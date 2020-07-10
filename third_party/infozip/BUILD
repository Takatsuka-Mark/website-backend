ZIP_DIRECTORY = "zip30"

ZIP_LIB_SRCS = [
    "%s/crc32.c" % ZIP_DIRECTORY,
    "%s/crypt.c" % ZIP_DIRECTORY,
    "%s/deflate.c" % ZIP_DIRECTORY,
    "%s/fileio.c" % ZIP_DIRECTORY,
    "%s/globals.c" % ZIP_DIRECTORY,
    "%s/trees.c" % ZIP_DIRECTORY,
    "%s/ttyio.c" % ZIP_DIRECTORY,
    "%s/util.c" % ZIP_DIRECTORY,
    "%s/zbz2err.c" % ZIP_DIRECTORY,
    "%s/zip.c" % ZIP_DIRECTORY,
    "%s/zipfile.c" % ZIP_DIRECTORY,
    "%s/zipup.c" % ZIP_DIRECTORY,
    "%s/unix/unix.c" % ZIP_DIRECTORY,
]

ZIP_LIB_HDRS = [
    "%s/crc32.h" % ZIP_DIRECTORY,
    "%s/crypt.h" % ZIP_DIRECTORY,
    "%s/ebcdic.h" % ZIP_DIRECTORY,
    "%s/revision.h" % ZIP_DIRECTORY,
    "%s/tailor.h" % ZIP_DIRECTORY,
    "%s/ttyio.h" % ZIP_DIRECTORY,
    "%s/zip.h" % ZIP_DIRECTORY,
    "%s/ziperr.h" % ZIP_DIRECTORY,
    "%s/unix/osdep.h" % ZIP_DIRECTORY,
    "%s/unix/zipup.h" % ZIP_DIRECTORY,
]

UNZIP_DIRECTORY = "unzip60"

UNZIP_LIB_SRCS = [
    "%s/apihelp.c" % UNZIP_DIRECTORY,
    "%s/crc32.c" % UNZIP_DIRECTORY,
    "%s/crypt.c" % UNZIP_DIRECTORY,
    "%s/envargs.c" % UNZIP_DIRECTORY,
    "%s/explode.c" % UNZIP_DIRECTORY,
    "%s/extract.c" % UNZIP_DIRECTORY,
    "%s/fileio.c" % UNZIP_DIRECTORY,
    #"%s/gbloffs.c" % UNZIP_DIRECTORY,
    "%s/globals.c" % UNZIP_DIRECTORY,
    "%s/inflate.c" % UNZIP_DIRECTORY,
    "%s/list.c" % UNZIP_DIRECTORY,
    "%s/match.c" % UNZIP_DIRECTORY,
    "%s/process.c" % UNZIP_DIRECTORY,
    "%s/timezone.c" % UNZIP_DIRECTORY,
    "%s/ttyio.c" % UNZIP_DIRECTORY,
    "%s/ubz2err.c" % UNZIP_DIRECTORY,
    "%s/unreduce.c" % UNZIP_DIRECTORY,
    "%s/unshrink.c" % UNZIP_DIRECTORY,
    "%s/unzipstb.c" % UNZIP_DIRECTORY,
    "%s/unix/unix.c" % UNZIP_DIRECTORY,
    "%s/zipinfo.c" % UNZIP_DIRECTORY,
]

UNZIP_LIB_HDRS = [
    "%s/consts.h" % UNZIP_DIRECTORY,
    "%s/crc32.h" % UNZIP_DIRECTORY,
    "%s/crypt.h" % UNZIP_DIRECTORY,
    "%s/ebcdic.h" % UNZIP_DIRECTORY,
    "%s/globals.h" % UNZIP_DIRECTORY,
    "%s/inflate.h" % UNZIP_DIRECTORY,
    "%s/timezone.h" % UNZIP_DIRECTORY,
    "%s/ttyio.h" % UNZIP_DIRECTORY,
    "%s/unzip.h" % UNZIP_DIRECTORY,
    "%s/unzpriv.h" % UNZIP_DIRECTORY,
    "%s/unzvers.h" % UNZIP_DIRECTORY,
    "%s/zip.h" % UNZIP_DIRECTORY,
    "%s/unix/unxcfg.h" % UNZIP_DIRECTORY,
]

cc_library(
    name = "libzip",
    srcs = ZIP_LIB_SRCS + ZIP_LIB_HDRS,
    hdrs = ZIP_LIB_HDRS,
    copts = ["-I%s" % ZIP_DIRECTORY],
    defines = ["UNIX"],
    deps = ["//third_party/zlib"],
)

cc_binary(
    name = "zip",
    srcs = ["%s/zip.c" % ZIP_DIRECTORY],
    defines = ["UNIX"],
    visibility = ["//visibility:public"],
    deps = [":libzip"],
)

cc_library(
    name = "libunzip",
    srcs = UNZIP_LIB_SRCS + UNZIP_LIB_HDRS,
    hdrs = UNZIP_LIB_HDRS,
    copts = [
        "-Ithird_party/infozip/%s" % UNZIP_DIRECTORY,
        "-Ithird_party/infozip/%s/unix" % UNZIP_DIRECTORY,
    ],
    defines = [
        "LARGE_FILE_SUPPORT",
        "UNIX",
        "NO_LCHMOD",
    ],
    deps = ["//third_party/zlib"],
)

cc_binary(
    name = "unzip",
    srcs = ["%s/unzip.c" % UNZIP_DIRECTORY],
    defines = [
        "UNIX",
        "LARGE_FILE_SUPPORT",
    ],
    visibility = ["//visibility:public"],
    deps = [":libunzip"],
)
