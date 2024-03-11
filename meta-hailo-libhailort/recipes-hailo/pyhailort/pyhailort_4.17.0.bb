DESCRIPTION = "pyhailort - hailo's python API \
		the recipe depends on _pyhailort shared object compiles in pyhailort-shared \
		the recipe installed using pyhailort setuptools into python/site-packages \
		the recipe contains all the python dependencies and it's currently supported by python 3.6 and 3.7"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://../../../../LICENSE;md5=48b1c947c88868c23e4fb874890be6fc \
                    file://../../../../LICENSE-3RD-PARTY.md;md5=f491a052559dbcdae697362cd5a13c96"

SRC_URI = "git://git@github.com/hailo-ai/hailort.git;protocol=https;branch=v4.17.0-dev"
SRCREV = "c25e51785d90300b1fd0536717b860ab8963cf9e"

S = "${WORKDIR}/git/hailort/libhailort/bindings/python/platform"

DEPENDS = "python3 pyhailort-shared python3-wheel-native"

inherit setuptools3

RDEPENDS:${PN} += "libhailort python3-future python3-importlib-metadata python3-netifaces \
                   python3-appdirs python3-contextlib2 python3-netaddr \
                   python3-argcomplete python3-verboselogs python3-numpy python3-setuptools"

PACKAGE_NAME = "hailo_platform"

PY_VER = "${@ '${PYTHON_BASEVERSION}'.replace('.', '')}"

PYHAILORT_SHARED_FILE = "_pyhailort.cpython-${PY_VER}-${TARGET_ARCH}-linux-gnu.so"
PYHAILORT_SHARED_PATH = "${TMPDIR}/staging/lib"

# pyhailort shared object files are stripped, QA issue should be skipped
INSANE_SKIP:${PN} += "already-stripped"

# copy pyhailort shared object and hailort.h before configuration and compilation
do_configure:prepend() {
    cp ${PYHAILORT_SHARED_PATH}/${PYHAILORT_SHARED_FILE} ${S}/hailo_platform/pyhailort
    echo '{"py_version": "${PY_VER}", "arch": "${TARGET_ARCH}", "hrt_version": "${PV}"}' > ${S}/wheel_conf.json
}
