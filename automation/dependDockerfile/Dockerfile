FROM quay.io/openshift/origin-jenkins-agent-base:v4.0.0
# Jenkins slave requirements
RUN yum makecache \
 && yum erase -y java-1.8.0-openjdk-headless \
 && yum -y install openssl java-1.8.0-openjdk \
 && pushd /etc/pki/ca-trust/source/anchors \
 && curl -s -k -O https://password.corp.redhat.com/RH-IT-Root-CA.crt \
 && curl -s -k -O https://password.corp.redhat.com/legacy.crt \
 && curl -s -k -O https://engineering.redhat.com/Eng-CA.crt \
 && update-ca-trust extract \
 && openssl x509 -in RH-IT-Root-CA.crt -out RH-IT-Root-CA.der -outform der \
 && openssl x509 -in legacy.crt -out legacy.der -outform der \
 && openssl x509 -in Eng-CA.crt -out Eng-CA.der -outform der \
 && export JAVA_HOME="/usr/lib/jvm" \
 && export PATH="${PATH}:${JAVA_HOME}/jre/bin" \
 && keytool -importcert -noprompt -alias RH-IT-Root-CA -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -file RH-IT-Root-CA.der \
 && keytool -importcert -noprompt -alias legacy -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -file legacy.der \
 && keytool -importcert -noprompt -alias Eng-CA -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -file Eng-CA.der
ENTRYPOINT ["/usr/local/bin/run-jnlp-client"]
# General requirements                                                          
RUN yum install -y https://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm \
 && yum install -y gcc python-devel \
 && curl "https://bootstrap.pypa.io/get-pip.py" -o "get-pip.py" \
 && python get-pip.py \
 && yum install -y git \
 && git clone --depth 1 https://github.com/RedHatQE/CodeQuality
                           
# Python requirements                                                           
RUN yum install -y python-devel.x86_64 \
 && pip install coverage unittest2
                                                                                
# CPP requirements
RUN yum install -y gcc-c++ cppunit-devel.x86_64 cppunit.x86_64 unzip wget python-devel \
 && pip install gcovr

# Java requirements                                                             
RUN yum install -y maven 

ENV GOPATH="/gocode" PATH="~/.rbenv/versions/2.3.0/bin:/gocode/bin:$PATH"

# Golang requirements
RUN yum install -y golang \
 && mkdir -p gocode/src/github.com/ \
 && cp -r /CodeQuality/examples/golang-test-repo/ gocode/src/github.com/ \
 && cd gocode/src/github.com/ \
 && export PATH=/gocode/bin:$PATH \
 && export GOPATH=/gocode \
 && go get github.com/axw/gocov/gocov \
 && go get github.com/AlekSi/gocov-xml
                           
# Ruby requirements
RUN yum install -y ruby-devel rubygems-devel cmake make gcc \
 && yum install -y openssl-devel readline-devel zlib-devel \
 && git clone --depth 1 https://github.com/rbenv/rbenv.git ~/.rbenv \
 && cd ~/.rbenv && src/configure && make -C src && cd / \
 && git clone --depth 1 https://github.com/rbenv/ruby-build.git \
 && PREFIX=/usr/local ./ruby-build/install.sh \
 && ~/.rbenv/bin/rbenv install 2.3.0 \
 && export PATH=~/.rbenv/versions/2.3.0/bin:$PATH \
 && gem install bundler
