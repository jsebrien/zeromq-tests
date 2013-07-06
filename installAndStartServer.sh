#!/bin/bash

sudo apt-get -y update && sudo apt-get -y upgrade

wget --no-cookies --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com" "http://download.oracle.com/otn-pub/java/jdk/7u25-b15/jdk-7u25-linux-x64.tar.gz"
tar -xf jdk-7u25-linux-x64.tar.gz
sudo mkdir /usr/lib/jvm
sudo mv jdk1.7.0_25 /usr/lib/jvm/

JAVA_HOME=/usr/lib/jvm/jdk1.7.0_25/
export JAVA_HOME

sudo ln -s /usr/lib/jvm/jdk1.7.0_25/bin/jar /usr/bin/jar
sudo ln -s /usr/lib/jvm/jdk1.7.0_25/bin/java /usr/bin/java
sudo ln -s /usr/lib/jvm/jdk1.7.0_25/bin/javac /usr/bin/javac
sudo ln -s /usr/lib/jvm/jdk1.7.0_25/bin/javah /usr/bin/javah

sudo apt-get -y install maven
sudo apt-get -y install git

sudo apt-get -y install -y apache2 php5 libapache2-mod-php5 php-apc
sudo apt-get -y install -y libapache2-mod-proxy-html libxml2-dev php5-curl

wget http://download.zeromq.org/zeromq-3.2.3.tar.gz
tar -xvf zeromq-3.2.3.tar.gz

sudo apt-get -y install libtool autoconf automake
sudo apt-get -y install uuid-dev
sudo apt-get -y install build-essential gcc

cd zeromq-3.2.3
./configure
make
sudo make install
sudo ldconfig
cd ..

sudo apt-get -y install -y php5-dev pkg-config
git clone git://github.com/mkoppanen/php-zmq.git
cd php-zmq
phpize
./configure
make
sudo make install
cd ..


git clone https://github.com/zeromq/jzmq.git
cd jzmq
./autogen.sh
./configure
make
sudo make install
sudo ldconfig
cd ..

git clone https://github.com/jsebrien/zeromq-tests.git
cd zeromq-tests

sudo cp web/*.php /var/www

sudo chmod 666 /etc/php5/apache2/php.ini
echo -e "\nextension=zmq.so\n" >> /etc/php5/apache2/php.ini
sudo chmod 644 /etc/php5/apache2/php.ini

sudo service apache2 restart

export LD_LIBRARY_PATH=/usr/local/lib 
mvn install:install-file -Dfile=/usr/local/share/java/zmq.jar -DgroupId=org.zeromq -DartifactId=zeromq -Dversion=3.2.3 -Dpackaging=jar -DgeneratePom=true
mvn package
mvn exec:java -Dexec.mainClass="blog.hashmade.zeromq.main.RequestReplyServer" 
