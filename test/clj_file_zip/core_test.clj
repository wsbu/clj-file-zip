(ns clj-file-zip.core-test
  (:require [clojure.test :refer :all]
            [clj-file-zip.core :refer :all]))

(deftest zip-test
  (testing "zip"
    (let [input (tmp-file)
          output (tmp-dir)]
      (zip "test/resources/foo" (.getPath input))
      (unzip (.getPath input) (str (.getPath output) "/"))
      (is (= (slurp (str (.getPath output) "/foo")) "file: foo\n")))))
