FROM --platform=$BUILDPLATFORM debian AS paper

RUN apt-get update
RUN apt-get install jq curl wget -y

WORKDIR /paper

COPY test/paper/install.sh ./install.sh

RUN chmod +x ./install.sh && ./install.sh
RUN rm ./install.sh -rf


FROM --platform=$BUILDPLATFORM ghcr.io/graalvm/jdk-community:24 AS prerun

WORKDIR /paper

COPY --from=paper /paper/ .

COPY test/paper/bukkit.yml .
COPY test/paper/spigot.yml .
COPY test/paper/server.properties .

RUN java -jar paperclip.jar --nogui

FROM --platform=$BUILDPLATFORM ghcr.io/graalvm/jdk-community:24 AS run

WORKDIR /paper

COPY --from=prerun /paper/ /paper

COPY plugin/build/libs /paper/plugins

EXPOSE 25565

ENV JAVA_XMX="2G"
ENV JAVA_XMS="2G"
ENV MINECRAFT_EULA_AGREE="true"

CMD ["sh", "-c", "java -Xmx${JAVA_XMX} -Xms${JAVA_XMX} -XX:+UnlockExperimentalVMOptions -XX:+UseJVMCICompiler -Dcom.mojang.eula.agree=${MINECRAFT_EULA_AGREE} -jar paperclip.jar --nogui"]