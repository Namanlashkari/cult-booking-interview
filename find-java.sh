# Sourced by run-tests.sh / run-app.sh. Finds a REAL JDK and sets $JAVAC / $JAVA.
#
# Why this exists: on some machines the `java`/`javac` on the PATH (and even
# macOS's /usr/libexec/java_home) are stubs that print "Unable to locate a Java
# Runtime" and fail. So we don't trust a candidate just because it exists — we
# actually run `javac -version` and only accept one that succeeds.

_works() {
  # $1 = path to a javac binary. True only if it runs without error.
  [ -x "$1" ] && "$1" -version >/dev/null 2>&1
}

_find_javac() {
  # 1. Respect an explicit JAVA_HOME if it works.
  if [ -n "$JAVA_HOME" ] && _works "$JAVA_HOME/bin/javac"; then
    echo "$JAVA_HOME/bin/javac"; return 0
  fi

  # 2. macOS: ask the OS for a registered JDK.
  if [ -x /usr/libexec/java_home ]; then
    _jh="$(/usr/libexec/java_home 2>/dev/null)"
    if [ -n "$_jh" ] && _works "$_jh/bin/javac"; then
      echo "$_jh/bin/javac"; return 0
    fi
  fi

  # 3. Common Homebrew / Linux install locations.
  for _p in \
    /opt/homebrew/opt/openjdk /opt/homebrew/opt/openjdk@21 /opt/homebrew/opt/openjdk@17 \
    /usr/local/opt/openjdk /usr/local/opt/openjdk@21 /usr/local/opt/openjdk@17 \
    /usr/lib/jvm/default-java /usr/lib/jvm/java-21-openjdk /usr/lib/jvm/java-17-openjdk; do
    if _works "$_p/bin/javac"; then
      echo "$_p/bin/javac"; return 0
    fi
  done

  # 4. Whatever javac is on the PATH — but only if it actually runs.
  _pj="$(command -v javac 2>/dev/null)"
  if [ -n "$_pj" ] && _works "$_pj"; then
    echo "$_pj"; return 0
  fi

  return 1
}

JAVAC="$(_find_javac)"
if [ -z "$JAVAC" ]; then
  echo "ERROR: could not find a working JDK (need Java 17+)." >&2
  echo "Install one (e.g. 'brew install openjdk') or set JAVA_HOME to a JDK, then re-run." >&2
  exit 1
fi
JAVA="$(dirname "$JAVAC")/java"
echo "Using JDK at: $(dirname "$(dirname "$JAVAC")")"
