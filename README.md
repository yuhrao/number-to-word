# number-to-words
A library to convert a number into words.

![Clojars Version](https://img.shields.io/clojars/v/io.github.yuhribernardes/number-to-words?color=%2336B8DD&logo=Clojure&logoColor=white&style=for-the-badge)

## Installation

```edn
[io.github.yuhribernardes/number-to-words "0.2.0"]
```

## Usage

### Basic usage

``` clojure
(ns my-ns
 (:require [number-to-words.parse :as ntw]
           [number-to-words.client :as ntw-cli]))
 
(def client (ntw-cli/create :en)) ;; create a client for the desired language

(ntw/number->words client 123) ;;=> "one hundred and twenty three"
```

### Extending your own language

``` clojure
(ns my-ns
 (:require [number-to-words.parse :as ntw]
           [number-to-words.client :as ntw-cli]))

(def my-specs (ntw-cli/assoc-lang
               ntw-cli/specs
               :pt
               {:zero-case "zero"
                :natural {1 "um"
                          2 "dois"
                          3 "três"
                          4 "quatro"}
                :pows ["milhão" "mil" nil]}))

(def my-client (ntw-cli/create my-specs :pt))

(ntw/number->words my-client 0) ;;=> "zero"
(ntw/number->words my-client 1) ;;=> "um"
(ntw/number->words my-client 2) ;;=> "dois"
(ntw/number->words my-client 3) ;;=> "três"
(ntw/number->words my-client 4) ;;=> "quatro"
```

## Supported language
- English (`:en`)

## Deploy

1. Configure auth envs

``` sh
export CLOJARS_USER=<your-clojars-username>
export CLOJARS_PWD=<your-deploy-token>
```

> Notice you need to generate deploy tokens at your clojars account

2. Deploy it!!

``` sh
lein deploy release
```

> You can also see deploy options [here][clojars-deploy]

## Tests

### Standalone

``` sh
lein kaocha
```

### For development
It's highly recommended to run tests in watch mode to see what is happening while making changes in the code

``` sh
lein kaocha --watch
```

## Development environment

### Default profiles

You need to configure your repl to start with te profiles described in the list below:
- kaocha

If you use Emacs, it's highly recommended to create a `dir-locals.el` file with the given content below:

``` emacs-lisp
((clojure-mode
  (cider-lein-global-options . "with-profile +kaocha")))
```

## To Do

- [ ] Auto generate library version based on [Semantic Versioning][semver]
- [ ] Better namespace organizations (move core functions to their own namespaces)
- [ ] Add better Clojure Docs
- [x] Configure clj-kondo to not throw errors to some forms (e.g. nubank matcher-combinators `match?`)

## License

Copyright © 2022

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.

[semver]: https://semver.org
[clojars-deploy]: https://github.com/technomancy/leiningen/blob/master/doc/DEPLOY.md#deploying-libraries
