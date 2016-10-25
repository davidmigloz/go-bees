###############################################################################
#                 Instal OpenCV in Travis from source code                    #
###############################################################################
# In travis.yml must be:                                                      #
# sudo: required                                                              #
# jdk: oraclejdk8                                                             #
# compiler: gcc                                                               #
# before_install: sudo apt-get update                                         #
# install:                                                                    #
#   - sudo apt-get install python-numpy                                       #
###############################################################################

wget https://github.com/opencv/opencv/archive/3.1.0.zip
unzip 3.1.0.zip >/dev/null
mv opencv-3.1.0 opencv
cd opencv
mkdir build
cd build
cmake -DCMAKE_BUILD_TYPE=Release -DCMAKE_INSTALL_PREFIX=/usr/local -DBUILD_SHARED_LIBS=OFF -DBUILD_EXAMPLES=OFF -DBUILD_TESTS=OFF -DBUILD_PERF_TESTS=OFF -DBUILD_opencv_python=OFF ..
make -j8 >/dev/null
sudo make -j8 install >/dev/null
cd ../..
mkdir opencv_lib
cp opencv/build/lib/libopencv_java310.so opencv_lib/libopencv_java310.so
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$PWD/opencv_lib/