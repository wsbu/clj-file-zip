(ns clj-file-zip.core-test
  (:require [clojure.test :refer :all]
            [clj-file-zip.core :refer :all]
            [clojure.java.io :as io]))

(deftest zip-unzip-test
  (testing "zip-unzip"
    (let [input (tmp-file)
          output (tmp-dir)]
      (zip "test/resources/foo" (.getPath input))
      (unzip (.getPath input) (str (.getPath output) "/"))
      (is (= (slurp (str (.getPath output) "/foo")) "file: foo\n"))
      (dorun (map #(delete-file (.getPath %)) [input output])))
    (let [archive (tmp-file)
          extract-destination (tmp-dir)]
      (zip "test/resources/folder1" (.getPath archive))
      (unzip (.getPath archive) (str (.getPath extract-destination) "/"))
      (is (= (slurp (str (.getPath extract-destination)
                         "/folder1/folder2/folder2.txt"))
             "file: folder2.txt\n"))
      (dorun (map #(delete-file (.getPath %)) [archive extract-destination])))))

(deftest zip-files-test
  (testing "zip-files"
    (let [archive (tmp-file)
          destination (tmp-dir)]
      (zip-files ["test/resources/foo" "test/resources/folder1/folder1.txt"]
                 (.getPath archive))
      (unzip (.getPath archive) (str (.getPath destination) "/"))
      (is (= (slurp (str (.getPath destination) "/foo")) "file: foo\n"))
      (is (= (slurp (str (.getPath destination) "/folder1.txt"))
             "file: folder1.txt\n"))
      (dorun (map #(delete-file (.getPath %)) [archive destination])))))

(deftest zip-unzip-encrypted-test
  (testing "zip-unzip-encrypted"
    (let [archive (tmp-file)
          destination (tmp-dir)]
      (zip-encrypted "test/resources/folder1" (str (.getPath archive) ".zip")
                     "pass123")
      (is (thrown? java.util.zip.ZipException
                   (unzip (str (.getPath archive) ".zip")
                          (str (.getPath destination) "/"))))
      (unzip-encrypted (str (.getPath archive) ".zip")
                       (str (.getPath destination) "/") "pass123")
      (is (= (slurp (str (.getPath destination) "/folder1/folder2/folder2.txt"))
             "file: folder2.txt\n"))
      (dorun (map #(delete-file (.getPath %)) [archive destination
                                               (io/file (str (.getPath archive)
                                                             ".zip"))])))))
