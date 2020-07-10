package(
    default_visibility = [
        "//visibility:public",
    ],
)

java_library(
    name = "guava",
    exports = [
        "@maven//:com_google_guava_guava",
    ],
)

java_library(
    name = "picocli",
    exports = [
        "@maven//:info_picocli_picocli",
    ],
)

java_library(
    name = "apache_httpcomponents_httpclient",
    exports = [
        "@maven//:org_apache_httpcomponents_httpclient",
    ],
)

java_library(
    name = "apache_commons_lang3",
    exports = [
        "@maven//:org_apache_commons_commons_lang3",
    ],
)

java_library(
    name = "com_amazonaws_java_sdk_bundle",
    exports = [
        "@maven//:com_amazonaws_aws_java_sdk_bundle",
    ],
)

java_library(
    name = "com_amazonaws_dynamodb_streams_kinesis_adapter",
    visibility = ["//search/indexer:__subpackages__"],
    exports = [
        "@maven//:com_amazonaws_dynamodb_streams_kinesis_adapter",
    ],
    runtime_deps = [
        ":com_amazonaws_amazon_kinesis_client",
    ],
)

java_library(
    name = "com_amazonaws_amazon_kinesis_client",
    visibility = ["//search/indexer:__subpackages__"],
    exports = [
        "@maven//:com_amazonaws_amazon_kinesis_client",
    ],
)

java_library(
    name = "com_auth0_java_jwt",
    exports = [
        "@maven//:com_auth0_java_jwt",
    ],
)

java_library(
    name = "com_auth0_jwks_rsa",
    exports = [
        "@maven//:com_auth0_jwks_rsa",
    ],
)

java_library(
    name = "com_beust_jcommander",
    exports = [
        "@maven//:com_beust_jcommander",
    ],
)

java_library(
    name = "com_fasterxml_jackson_annotations",
    exports = [
        "@maven//:com_fasterxml_jackson_core_jackson_annotations",
    ],
)

java_library(
    name = "com_fasterxml_jackson_core",
    exports = [
        "@maven//:com_fasterxml_jackson_core_jackson_core",
    ],
)

java_library(
    name = "com_fasterxml_jackson_databind",
    exports = [
        "@maven//:com_fasterxml_jackson_core_jackson_databind",
    ],
)

java_library(
    name = "com_fasterxml_jackson_datatypes",
    exports = [
        "@maven//:com_fasterxml_jackson_datatype_jackson_datatype_jdk8",
        "@maven//:com_fasterxml_jackson_datatype_jackson_datatype_jsr310",
    ],
)

java_library(
    name = "com_google_auto_service",
    exports = [
        "@maven//:com_google_auto_service_auto_service",
        "@maven//:com_google_auto_service_auto_service_annotations",
    ],
)

java_library(
    name = "com_google_auth_oauth",
    testonly = 1,
    exports = [
        "@maven//:com_google_auth_google_auth_library_oauth2_http",
    ],
)

java_library(
    name = "com_google_inject_guice",
    exports = [
        "@maven//:com_google_inject_guice",
    ],
)
java_library(
    name = "com_google_jimfs",
    exports = [
        "@maven//:com_google_jimfs_jimfs",
    ],
)

java_library(
    name = "com_google_oauth_client",
    exports = [
        "@maven//:com_google_http_client_google_http_client_jackson2",
        "@maven//:com_google_oauth_client_google_oauth_client",
    ],
)

java_library(
    name = "com_google_re2j",
    exports = [
        "@maven//:com_google_re2j_re2j",
    ],
)

java_library(
    name = "com_google_protobuf_java",
    exports = [
        "@maven//:com_google_protobuf_protobuf_java",
    ],
)

java_library(
    name = "com_google_protobuf_java_util",
    exports = [
        "@maven//:com_google_protobuf_protobuf_java_util",
    ],
)

java_library(
    name = "com_google_truth",
    testonly = 1,
    exports = [
        "@maven//:com_google_truth_truth",
    ],
)

java_library(
    name = "com_google_truth_java8",
    testonly = 1,
    exports = [
        "@maven//:com_google_truth_extensions_truth_java8_extension",
    ],
)

java_library(
    name = "com_google_truth_proto",
    testonly = 1,
    exports = [
        "@maven//:com_google_truth_extensions_truth_proto_extension",
    ],
)

java_library(
    name = "com_hubspot_jackson_protobuf",
    exports = [
        "@maven//:com_hubspot_jackson_jackson_datatype_protobuf",
    ],
)

java_library(
    name = "com_zaxxer_hikaricp",
    exports = [
        "@maven//:com_zaxxer_HikariCP",
    ],
)

java_library(
    name = "docker_junit_rule",
    testonly = 1,
    exports = [
        "@maven//:com_arakelian_docker_junit_rule",
    ],
    runtime_deps = [
        "@maven//:com_sun_activation_javax_activation",
    ],
)

java_library(
    name = "dropwizard_metrics_core",
    exports = [
        "@maven//:io_dropwizard_metrics5_metrics_core",
    ],
)

java_library(
    name = "com_esotericsoftware_kryo_kryo",
    exports = [
        "@maven//:com_esotericsoftware_kryo_kryo",
    ],
)

java_library(
    name = "io_kubernetes_client_java",
    exports = [
        "@maven//:io_kubernetes_client_java",
        "@maven//:io_kubernetes_client_java_api",
    ],
)

java_library(
    name = "io_springfox_swagger",
    exports = [
        "@maven//:io_springfox_springfox_swagger2",
    ],
)

java_library(
    name = "io_springfox_swagger_ui",
    exports = [
        "@maven//:io_springfox_springfox_swagger_ui",
    ],
)

java_library(
    name = "io_swagger_annotations",
    exports = [
        "@maven//:io_swagger_swagger_annotations",
    ],
)

java_library(
    name = "javax_annotation_api",
    exports = [
        "@maven//:javax_annotation_javax_annotation_api",
    ],
)

java_library(
    name = "javax_inject",
    exports = [
        "@maven//:javax_inject_javax_inject",
    ],
)

java_library(
    name = "javax_validation_api",
    exports = [
        "@maven//:javax_validation_validation_api",
    ],
)

java_library(
    name = "org_glassfish_jakarta_el",
    exports = [
        "@maven//:org_glassfish_jakarta_el",
    ],
)

java_library(
    name = "org_hibernate_validator_hibernate_validator",
    exports = [
        "@maven//:org_hibernate_validator_hibernate_validator",
    ],
)

java_library(
    name = "javax_servlet_api",
    exports = [
        "@maven//:javax_servlet_javax_servlet_api",
    ],
)

java_library(
    name = "javax_xml_bind_jaxb_api",
    exports = [
        "@maven//:javax_xml_bind_jaxb_api",
    ],
)

java_library(
    name = "junit",
    testonly = 1,
    exports = [
        "@maven//:junit_junit",
    ],
)

java_library(
    name = "logback",
    exports = [
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:ch_qos_logback_logback_core",
    ],
)

java_library(
    name = "mariadb_java_client",
    exports = [
        "@maven//:org_mariadb_jdbc_mariadb_java_client",
    ],
)

java_library(
    name = "org_apache_flink_flink_java",
    exports = [
        "@maven//:org_apache_flink_flink_java",
    ],
)

java_library(
    name = "org_apache_flink_flink_clients_2_11",
    exports = [
        "@maven//:org_apache_flink_flink_clients_2_11",
    ],
)

java_library(
    name = "org_apache_flink_flink_test_utils_junit",
    exports = [
        "@maven//:org_apache_flink_flink_test_utils_junit",
    ],
)

java_library(
    name = "org_apache_hadoop_aws",
    exports = [
        "@maven//:org_apache_hadoop_hadoop_aws",
    ],
)

java_library(
    name = "org_apache_hadoop_common",
    exports = [
        "@maven//:org_apache_hadoop_hadoop_common",
    ],
)

java_library(
    name = "org_apache_hadoop_mapreduce_client_core",
    exports = [
        "@maven//:org_apache_hadoop_hadoop_mapreduce_client_core",
    ],
    runtime_deps = [
        ":org_apache_hadoop_common",
    ],
)

java_library(
    name = "org_apache_parquet_protobuf",
    exports = [
        "@maven//:org_apache_parquet_parquet_protobuf",
    ],
    runtime_deps = [
        ":org_apache_hadoop_mapreduce_client_core",
    ],
)

java_library(
    name = "io_prometheus",
    exports = [
        "@maven//:io_prometheus_simpleclient",
        "@maven//:io_prometheus_simpleclient_hotspot",
        "@maven//:io_prometheus_simpleclient_pushgateway",
    ],
)

java_library(
    name = "org_bouncycastle",
    exports = [
        "@maven//:org_bouncycastle_bcpkix_jdk15on",
        "@maven//:org_bouncycastle_bcprov_jdk15on",
    ],
)

java_library(
    name = "org_elasticsearch_rest_high_level_client",
    exports = [
        "@maven//:org_elasticsearch_client_elasticsearch_rest_high_level_client",
    ],
)

java_library(
    name = "org_flywaydb_core",
    exports = [
        "@maven//:org_flywaydb_flyway_core",
    ],
)

java_binary(
    name = "org_flywaydb_commandline",
    main_class = "org.flywaydb.commandline.Main",
    runtime_deps = [
        "@maven//:org_flywaydb_flyway_commandline",
        "@maven//:org_postgresql_postgresql",
    ],
)

java_library(
    name = "org_freemarker",
    exports = [
        "@maven//:org_freemarker_freemarker",
    ],
    runtime_deps = [
        ":org_slf4j_log4j_over_slf4j",
    ],
)

java_library(
    name = "org_hyperledger_fabric_sdk_java",
    exports = [
        # Watch https://github.com/bazelbuild/rules_jvm_external/issues/147 for this scope=compile
        # dependency.
        "@maven//:io_opencensus_opencensus_contrib_grpc_metrics",
        "@maven//:io_perfmark_perfmark_api",
        "@maven//:org_hyperledger_fabric_sdk_java_fabric_sdk_java",
    ],
)

java_library(
    name = "org_jooq",
    exports = [
        "@maven//:org_jooq_jooq",
    ],
)

java_binary(
    name = "org_jooq_codegen",
    main_class = "org.jooq.codegen.GenerationTool",
    runtime_deps = [
        "@maven//:com_sun_activation_javax_activation",
        "@maven//:com_sun_xml_bind_jaxb_core",
        "@maven//:com_sun_xml_bind_jaxb_impl",
        "@maven//:javax_xml_bind_jaxb_api",
        "@maven//:org_jooq_jooq",
        "@maven//:org_jooq_jooq_codegen",
        "@maven//:org_jooq_jooq_meta",
        "@maven//:org_postgresql_postgresql",
    ],
)

java_binary(
    name = "com_helger_parser_generator_cc",
    main_class = "com.helger.pgcc.main.javacc",
    runtime_deps = [
        "@maven//:com_helger_parser_generator_cc",
    ],
)

java_library(
    name = "org_junit_jupiter_api",
    testonly = 1,
    exports = [
        "@maven//:org_junit_jupiter_junit_jupiter_api",
    ],
)

java_library(
    name = "org_mockito_mockito_core",
    exports = [
        "@maven//:org_mockito_mockito_core",
    ],
)

java_library(
    name = "org_postgresql_postgresql",
    exports = [
        "@maven//:org_postgresql_postgresql",
    ],
)

java_library(
    name = "org_roaringbitmap",
    exports = [
        "@maven//:org_roaringbitmap_RoaringBitmap",
    ],
)

java_library(
    name = "org_slf4j",
    exports = [
        "@maven//:org_slf4j_slf4j_api",
    ],
)

java_library(
    name = "org_slf4j_log4j_over_slf4j",
    exports = [
        "@maven//:org_slf4j_log4j_over_slf4j",
    ],
)

java_library(
    name = "org_springframework_context",
    exports = [
        "@maven//:org_springframework_spring_context",
    ],
)

java_library(
    name = "org_springframework_spring_web",
    exports = [
        "@maven//:org_springframework_spring_web",
    ],
)

java_library(
    name = "org_springframework_boot_autoconfigure",
    exports = [
        "@maven//:org_springframework_boot_spring_boot_autoconfigure",
    ],
)

java_library(
    name = "org_springframework_boot_loader",
    exports = [
        "@maven//:org_springframework_boot_spring_boot_loader",
    ],
)

java_library(
    name = "org_springframework_boot_starter",
    exports = [
        "@maven//:org_springframework_boot_spring_boot_starter",
    ],
)

java_library(
    name = "org_springframework_boot_starter_freemarker",
    exports = [
        "@maven//:org_springframework_boot_spring_boot_starter_freemarker",
    ],
)

java_library(
    name = "org_springframework_boot_starter_jooq",
    exports = [
        "@maven//:org_springframework_boot_spring_boot_starter_jooq",
    ],
)

java_library(
    name = "org_springframework_boot_starter_security",
    exports = [
        "@maven//:org_springframework_boot_spring_boot_starter_security",
    ],
)

java_library(
    name = "org_springframework_boot_starter_test",
    testonly = 1,
    exports = [
        "@maven//:org_springframework_boot_spring_boot_starter_test",
    ],
)

java_library(
    name = "org_springframework_boot_starter_web",
    exports = [
        "@maven//:org_springframework_boot_spring_boot_starter_jetty",
        "@maven//:org_springframework_boot_spring_boot_starter_web",
    ],
)

java_library(
    name = "org_springframework_security_oauth_boot_autoconfigure",
    exports = [
        "@maven//:org_springframework_security_oauth_boot_spring_security_oauth2_autoconfigure",
    ],
)

java_library(
    name = "org_web3j_core",
    exports = [
        "@maven//:org_web3j_core",
        "@maven//:org_web3j_utils",
    ],
)

java_library(
    name = "org_web3j_quorum",
    exports = [
        "@maven//:org_web3j_quorum",
    ],
)

java_library(
    name = "software_amazon_awssdk_apache_client",
    exports = [
        "@maven//:software_amazon_awssdk_apache_client",
    ],
)

java_library(
    name = "software_amazon_awssdk_netty_nio_client",
    exports = [
        "@maven//:software_amazon_awssdk_netty_nio_client",
    ],
)

java_library(
    name = "software_amazon_awssdk_athena",
    exports = [
        "@maven//:software_amazon_awssdk_athena",
    ],
)

java_library(
    name = "software_amazon_awssdk_cloudformation",
    exports = [
        "@maven//:software_amazon_awssdk_cloudformation",
    ],
)

java_library(
    name = "software_amazon_awssdk_cloudfront",
    exports = [
        "@maven//:software_amazon_awssdk_cloudfront",
    ],
)

java_library(
    name = "software_amazon_awssdk_codedeploy",
    exports = [
        "@maven//:software_amazon_awssdk_codedeploy",
    ],
)

java_library(
    name = "software_amazon_awssdk_cloudwatch",
    exports = [
        "@maven//:software_amazon_awssdk_cloudwatch",
    ],
)

java_library(
    name = "software_amazon_awssdk_sdk_core",
    exports = [
        "@maven//:software_amazon_awssdk_sdk_core",
    ],
)

java_library(
    name = "software_amazon_awssdk_dynamodb",
    exports = [
        "@maven//:software_amazon_awssdk_dynamodb",
    ],
)

java_library(
    name = "software_amazon_awssdk_ec2",
    exports = [
        "@maven//:software_amazon_awssdk_ec2",
    ],
)

java_library(
    name = "software_amazon_awssdk_ecr",
    exports = [
        "@maven//:software_amazon_awssdk_ecr",
    ],
)

java_library(
    name = "software_amazon_awssdk_elasticache",
    exports = [
        "@maven//:software_amazon_awssdk_elasticache",
    ],
)

java_library(
    name = "software_amazon_awssdk_elasticsearch",
    exports = [
        "@maven//:software_amazon_awssdk_elasticsearch",
    ],
)

java_library(
    name = "software_amazon_awssdk_iam",
    exports = [
        "@maven//:software_amazon_awssdk_iam",
    ],
)

java_library(
    name = "software_amazon_awssdk_kinesis",
    exports = [
        "@maven//:software_amazon_awssdk_kinesis",
    ],
)

java_library(
    name = "software_amazon_awssdk_redshift",
    exports = [
        "@maven//:software_amazon_awssdk_redshift",
    ],
)

java_library(
    name = "software_amazon_awssdk_rds",
    exports = [
        "@maven//:software_amazon_awssdk_rds",
    ],
)

java_library(
    name = "software_amazon_awssdk_rdsdata",
    exports = [
        "@maven//:software_amazon_awssdk_rdsdata",
    ],
)

java_library(
    name = "software_amazon_awssdk_ssm",
    exports = [
        "@maven//:software_amazon_awssdk_ssm",
    ],
)

java_library(
    name = "software_amazon_awssdk_route53",
    exports = [
        "@maven//:software_amazon_awssdk_route53",
    ],
)

java_library(
    name = "software_amazon_awssdk_s3",
    exports = [
        "@maven//:software_amazon_awssdk_s3",
    ],
)

java_library(
    name = "software_amazon_awssdk_ses",
    exports = [
        "@maven//:software_amazon_awssdk_ses",
    ],
)

java_library(
    name = "software_amazon_awssdk_sns",
    exports = [
        "@maven//:software_amazon_awssdk_sns",
    ],
)

java_library(
    name = "software_amazon_awssdk_sqs",
    exports = [
        "@maven//:software_amazon_awssdk_sqs",
    ],
)

java_library(
    name = "software_amazon_awssdk_sts",
    exports = [
        "@maven//:software_amazon_awssdk_sts",
    ],
)

java_library(
    name = "software_amazon_awssdk_cognitoidentityprovider",
    exports = [
        "@maven//:software_amazon_awssdk_cognitoidentityprovider",
    ],
)

java_library(
    name = "software_amazon_kinesis_amazon_kinesis_client",
    exports = [
        "@maven//:software_amazon_kinesis_amazon_kinesis_client",
    ],
)

java_library(
    name = "sun_xml_bind_jaxb",
    exports = [
        "@maven//:com_sun_xml_bind_jaxb_core",
        "@maven//:com_sun_xml_bind_jaxb_impl",
    ],
    runtime_deps = [
        ":javax_xml_bind_jaxb_api",
    ],
)

java_binary(
    name = "parquet_tool",
    main_class = "org.apache.parquet.tools.Main",
    runtime_deps = [
        "@maven//:org_apache_hadoop_hadoop_aws_3_1_1",
        "@maven//:org_apache_hadoop_hadoop_common_3_1_1",
        "@maven//:org_apache_hadoop_hadoop_mapreduce_client_core_3_1_1",
        "@maven//:org_apache_parquet_parquet_tools",
    ],
)

java_library(
    name = "org_graalvm_sdk_graal_sdk",
    exports = [
        "@maven//:org_graalvm_sdk_graal_sdk",
    ],
)

java_library(
    name = "org_graalvm_js_js",
    exports = [
        "@maven//:org_graalvm_js_js",
    ],
)

java_library(
    name = "org_graalvm_js_js_scriptengine",
    exports = [
        "@maven//:org_graalvm_js_js_scriptengine",
    ],
)
