[![Build Status](https://travis-ci.org/orium/fiasco.svg?branch=master)](https://travis-ci.org/orium/fiasco)
[![Code Coverage](https://codecov.io/gh/orium/fiasco/branch/master/graph/badge.svg)](https://codecov.io/gh/orium/fiasco)
[![Github stars](https://img.shields.io/github/stars/orium/fiasco.svg?logo=github)](https://github.com/orium/fiasco/stargazers)
[![License](https://img.shields.io/badge/license-MPL--2.0-blue.svg)](./LICENSE.md)

# Fiasco

Fiasco is a library that tries to offer a better way to deal with errors in Scala.  It is designed to follow these
principles:

1. Errors should have contextual debugging information.
   * It is nice to have a stacktrace on your logs.
2. Errors should not be throwable.
   * Throwing exceptions is to be avoided, and having non-throwable errors expresses that intention and avoids misuse.
3. Most times errors should be explicitly represented in the type system.
   * Hiding errors in a `Try`, `Future`, or a `{Monad,Applicative}Error` is, in general, not great.
4. Most times errors should have precise types.
   * Ideally they should be ADTs, since having a open world of possible errors complicates error recovery. 

Fiasco defines a `Fail` trait to represent errors, which follows principles 1 and 2.  Principles 3 and 4 are not
enforced by Fiasco, but it tries to make it easy and ergonomic to follow them.  The [Usage section](#usage)  will show
you how.

## Setup

To use fiasco add the following to your `build.sbt`:

```scala
resolvers += Resolver.bintrayRepo("orium", "maven")
libraryDependencies += "fiasco" %% "fiasco-core" % "<version>"
```

This package offers everything you need to get started, and has no dependencies.  There are, however, other packages to
make it more convenient to use Fiasco with other libraries you might be using:

```scala
libraryDependencies += "fiasco" %% "fiasco-cats-1"          % "<version>"
libraryDependencies += "fiasco" %% "fiasco-cats-effect-1"   % "<version>"
libraryDependencies += "fiasco" %% "fiasco-scalaz-7"        % "<version>"
libraryDependencies += "fiasco" %% "fiasco-zio-1"           % "<version>"
libraryDependencies += "fiasco" %% "fiasco-scala-logging-3" % "<version>"
```

## Usage

WIP!
