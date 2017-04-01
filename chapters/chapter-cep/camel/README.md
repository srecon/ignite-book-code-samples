> add following dependencies
    <dependency>
       <groupId>org.apache.ignite</groupId>
       <artifactId>ignite-indexing</artifactId>
       <version>${ignite.version}</version>
     </dependency>
     <!-- h2 db-->
     <dependency>
       <groupId>com.h2database</groupId>
       <artifactId>h2</artifactId>
       <version>1.3.175</version>
     </dependency>
     
     
> run java -jar ./target/node-runner-runnable.jar to run node
> run java -jar ~/Development/workshop/assembla/ignite-book/chapters/chapter-cep/target/streamer-runnable.jar to run simple streamer
> run java -jar ~/Development/workshop/assembla/ignite-book/chapters/chapter-cep/target/query-runnable.jar to run Query Status
Camel Intgeration
> to start jetty endpoint add camel jetty maven dependency
