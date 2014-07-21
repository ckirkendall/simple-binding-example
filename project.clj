(defproject binding-example "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://exampl.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2234"]
                 [ring/ring-core "1.3.0"]
                 [ring/ring-jetty-adapter "1.3.0"]
                 [enfocus "2.1.0"]]
  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]
            [lein-ring "0.8.11"]]
  :cljsbuild {:builds [{:source-paths ["src"],
                        :compiler {:output-to "resources/public/js/main.js"}}]}
  :ring {:handler binding-example.server/app})
