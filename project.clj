(defproject com.fluree/abracad "0.4.19"
  :description "De/serialize Clojure data structures with Avro."
  :url "https://github.com/fluree/abracad"
  :licenses [{:name "Eclipse Public License"
              :url  "http://www.eclipse.org/legal/epl-v10.html"}
             {:name "Apache License, Version 2.0"
              :url  "http://www.apache.org/licenses/LICENSE-2.0.html"}]

  :plugins [[codox/codox "0.10.8"]
            [lein-midje "3.2.2"]
            [lein-cloverage "1.2.2"]
            [lein-vanity "0.2.0"]]

  :repositories [["central" {:url "https://repo1.maven.org/maven2/" :snapshots false}]
                 ["clojars" {:url "https://clojars.org/repo/"}]]

  :global-vars {*warn-on-reflection* true}

  :dependencies [[org.apache.avro/avro "1.11.0"]
                 [cheshire/cheshire "5.10.1"]]

  :codox {:include [abracad.avro abracad.avro.edn]}

  :aliases {"coverage" ["cloverage" "-s" "coverage"]
            "loc"      ["vanity"]}

  :profiles {:dev  {:dependencies
                    [[midje "1.10.5" :exclusions [org.clojure/clojure]]
                     [org.xerial.snappy/snappy-java "1.1.8.4"]
                     [nubank/matcher-combinators "1.5.1"]]}
             :test {:dependencies
                    [[org.clojure/clojure "1.10.3"]
                     [midje "1.10.5" :exclusions [org.clojure/clojure]]
                     [org.xerial.snappy/snappy-java "1.1.8.4"]
                     [nubank/matcher-combinators "1.5.1"]]}}

  :source-paths ["src/clojure"]
  :java-source-paths ["src/java"]
  :test-paths ["test/"]

  :javac-options ["-target" "1.8" "-source" "1.8"])
