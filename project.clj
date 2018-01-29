(defproject dvlopt/utimbre
            "0.0.1"

  :description  "Utilities for Timbre"
  :url          "https://github.com/dvlopt/timbre"
  :license      {:name "Eclipse Public License"
                 :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[dvlopt/void "0.0.0"]
                 [dvlopt/ex   "1.0.0"]]
  :profiles     {:dev {:source-paths ["dev"]
                       :main         user
                       :dependencies [[org.clojure/clojure    "1.9.0"]
                                      [org.clojure/test.check "0.10.0-alpha2"]
                                      [criterium              "0.4.4"]
                                      [com.taoensso/timbre    "4.10.0"]]
                       :plugins      [[venantius/ultra "0.5.2"]
                                      [lein-codox      "0.10.3"]]
                       :codox        {:output-path  "doc/auto"
                                      :source-paths ["src"]}
                       :global-vars  {*warn-on-reflection* true}}})
