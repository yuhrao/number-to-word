(defproject io.github.yuhribernardes/number-to-words "0.2.2"
  :description "Library to convert number to words"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]]
  :main number-to-words.parse
  :target-path "target/%s"
  :aliases {"kaocha" ["with-profile" "+kaocha" "run" "-m" "kaocha.runner"]}
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
             :kaocha {:dependencies [[lambdaisland/kaocha "1.62.993"]
                                     [nubank/matcher-combinators "3.3.1"]]}}
  :repositories {"release" {:url "https://repo.clojars.org/"
                            :username :env/clojars_user
                            :password :env/clojars_pwd}})
