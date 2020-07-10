workspace(
    name = "backend",
)

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive", "http_file")
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")


RULES_JVM_EXTERNAL_TAG = "2.8"
RULES_JVM_EXTERNAL_SHA = "79c9850690d7614ecdb72d68394f994fef7534b292c4867ce5e7dec0aa7bdfad"

# 5.2.2 is used by Spring Boot 2.2.6
SPRING_BOOT_VERSION = "2.2.6.RELEASE"
SPRING_VERSION = "5.2.5.RELEASE"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:specs.bzl", "maven")
load("@rules_jvm_external//:defs.bzl", "maven_install")

org_springframework_boot_spring_boot_starter_web = maven.artifact(
    group = "org.springframework.boot",
    artifact = "spring-boot-starter-web",
    version = SPRING_BOOT_VERSION,
    exclusions = [
        maven.artifact(
            artifact = "spring-boot-starter-tomcat",
            group = "org.springframework.boot",
            version = SPRING_BOOT_VERSION,
        ),
    ],
)

maven_install(
    artifacts = [
        "com.google.code.findbugs:jsr305:1.3.9",
        "com.google.errorprone:error_prone_annotations:2.0.18",
        "com.google.j2objc:j2objc-annotations:1.1",

        "org.springframework:spring-context:%s" % SPRING_VERSION,
        "org.springframework:spring-web:%s" % SPRING_VERSION,
        "org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure:%s" % SPRING_BOOT_VERSION,
        "org.springframework.boot:spring-boot-autoconfigure:%s" % SPRING_BOOT_VERSION,
        "org.springframework.boot:spring-boot-loader:%s" % SPRING_BOOT_VERSION,
        "org.springframework.boot:spring-boot-starter:%s" % SPRING_BOOT_VERSION,
        "org.springframework.boot:spring-boot-starter-freemarker:%s" % SPRING_BOOT_VERSION,
        "org.springframework.boot:spring-boot-starter-jooq:%s" % SPRING_BOOT_VERSION,
        "org.springframework.boot:spring-boot-starter-test:%s" % SPRING_BOOT_VERSION,
        "org.springframework.boot:spring-boot-starter-jetty:%s" % SPRING_BOOT_VERSION,
        org_springframework_boot_spring_boot_starter_web,
        "org.springframework.boot:spring-boot-starter-security:%s" % SPRING_BOOT_VERSION,
    ],
    repositories = [
        "https://jcenter.bintray.com/",
        "https://repo1.maven.org/maven2",
    ],
)