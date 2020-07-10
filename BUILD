load("//tools/bzl:springboot.bzl", "springboot_jar")

java_library(
    name = "backend",
    srcs = glob(["src/main/java/**/*.java"]),
    resources = glob(["src/main/resources/**"]),
#    deps = ["//:all-external-targets"],
)

springboot_jar(
    name = "backend-boot",
    application_lib = ":backend",
    main_class = "com.takatsuka.web.WebApplication",
    tags = [
        "manual",
    ]
)