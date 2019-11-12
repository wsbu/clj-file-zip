(defproject net.redlion/clj-file-zip "0.1.0"
  :description "A simple Clojure library for dealing with zip files"
  :url "https://github.com/wsbu/clj-file-zip"
  :license {:name "GNU Lesser Genereal Public License"
            :url "https://www.gnu.org/licenses/lgpl-3.0.en.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [net.lingala.zip4j/zip4j "1.3.2"]]
  :deploy-repositories [["releases" {:url "https://mvn.redlion.net/repository/maven-releases"
                                     :sign-releases false}]
                        ["snapshots" {:url "https://mvn.redlion.net/repository/maven-snapshots"
                                      :sign-releases false}]])
