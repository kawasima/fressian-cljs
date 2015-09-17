(defproject net.unit8/fressian-cljs "0.2.0"
  :dependencies [[org.clojure/clojurescript "1.7.122" :scope "provided"]]
  :plugins [[lein-cljsbuild "1.1.0"]
            [com.cemerick/clojurescript.test "0.3.3"]]
  :source-paths ["src/clj" "src/cljs"]
  :cljsbuild
  { :builds 
    {:product
      { :source-paths ["src/cljs"]
        :jar true
        :compiler { :optimizations :whitespace
                    :output-to "target/fressian.js"}}
      :test
      { :source-paths ["src/cljs" "test/cljs"]
        :compiler { :optimizations :whitespace
                    :output-to "target/cljs/testable.js"
                    :pretty-print true}}}

    :test-commands
    { "unit" ["phantomjs" :runner
               "this.literal_js_was_evaulated=true"
               "target/cljs/testable.js"]}}
  :profiles
  {:dev
   {:dependencies [[org.clojure/clojure "1.7.0"]
                   [org.clojure/data.fressian "0.2.0"]]
    :test-paths ["test/clj"]}})
