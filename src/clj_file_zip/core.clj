(ns clj-file-zip.core
  (:import [java.util.zip ZipEntry ZipOutputStream ZipInputStream])
  (:require [clojure.java.io :as io]))

(defn zip-folder
  "Reads contents of folder (could be a single file or dir) and writes all data to output (ZipOutputStream).
   Entry name is the name of zipfile entry."
  ([folder entry-name output]
   (if (.isDirectory folder)
     (loop [children (vec (.listFiles folder))]
       (if (> (count children) 0)
         (do (zip-folder (first children) (str entry-name "/" (.getName (first children))) output)
             (recur (rest children)))))
     (let [in (io/input-stream folder)
           entry (ZipEntry. entry-name)]
       (.putNextEntry output entry)
       (io/copy in output)
       (.closeEntry output)
       (.close in)))))

(defn zip
  "Reads input-filename (could be a single file or a directory) and writes it to output-filename"
  ([input-filename output-filename entry-name]
   (with-open [out (ZipOutputStream. (io/output-stream output-filename))]
     (zip-folder (io/file input-filename) entry-name out)
     output-filename))
  ([input-filename output-filename]
   (zip input-filename output-filename (.getName (io/file input-filename)))
   output-filename))

(defn zip-files
  "Takes a collection of filenames and zips them into output-filename
   Eg: (zip-files \"/tmp/foo\" \"/tmp/foo.zip\")
   result: /tmp/foo.zip with the contents of /tmp/foo (file or directory)"
  [filenames output-filename]
  (with-open [out (ZipOutputStream. (io/output-stream output-filename))]
    (loop [files filenames]
      (if (> (count files) 0)
        (do (zip-folder (io/file (first files)) (.getName (io/file (first files))) out)
            (recur (rest files)))
        output-filename))))

(defn unzip
  "Unzips zip archive filename and write all contents to dir output-parent"
  [filename output-parent]
  (with-open [input (ZipInputStream. (io/input-stream filename))]
    (loop [entry (.getNextEntry input)]
      (if (not (= nil entry))
        (do
          (.mkdirs (.getParentFile (io/file (str output-parent (.getName entry)))))
        (let [output (io/output-stream (str output-parent (.getName entry)))]
          (io/copy input output)
          (.close output)
          (recur (.getNextEntry input))))))
    (.closeEntry input)))

(defn tmp-file
  "returns a temporary file object that will delete on exit"
  []
  (let [file (. java.io.File createTempFile "clj-file-zip-" ".tmp")]
    ;(.deleteOnExit file)
    file))

(defn tmp-dir
  "returns a temporary file object that will delete on exit"
  []
  (let [file (tmp-file)]
    (.delete file)
    (.mkdir file)
    ;(.deleteOnExit file)
    file))
