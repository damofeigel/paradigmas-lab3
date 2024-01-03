# INFORME LABORATORIO 3

##### Santiago Leon Torres Banner

### Feed Reader

Una vez clonado el repositorio para ejecutar el programa use:

```bash
$ ./gradlew run
```

Si es la primera vez que lo hace o no tiene instalado gradle en su equipo, tardara unos segundos extra en iniciar.

### Etapas de desarrollo

#### Estrategia

Mi estrategia seleccionada fueron busquedas en la internet en general.

#### Setear el entorno de trabajo

Lo primero a realizar fue ordenar el espacio de trabajo, para ello se me ocurrio utilizar alguna herramienta para manejar dependencias y librerias de Java, pues en el anterior laboratorio habia tenido algunos problemas con la inclusion de librerias externas.
La primer idea fue utilizar **Maven**, pero este fue descartado luego del primer dia, pues al instalarlo tiraba muchos errores, en especifico, a la hora de setear las variables de entorno `JAVA_HOME` y `M2_HOME` hice algun paso en falso y mi notebook termino cediendo ante un ["Ubuntu Login Loop"](https://www.makeuseof.com/fix-ubuntu-login-loop-issue/).

Lastimosamente ningun articulo pudo solucionar mi caso, asi que termine por formatear la notebook e instalar una version mas nueva de Ubuntu.

Al final, me decidi por **Gradle**, la cual es una herramienta muy similar a **Maven** pero mas nueva.

Tutoriales utiles para aprender lo basico de **Gradle**:

- [Gradle tutorial for complete beginners](https://www.youtube.com/watch?v=-dtcEMLNmn0)
- [Gradle Course for Beginners](https://www.youtube.com/watch?v=So0j4RnoKkU)

**Gradle** me permite no solo manejar liberias y dependencias para Java (y para otros lenguajes tambien) si no que con la ayuda del [_Gradle Wrapper_](https://docs.gradle.org/current/userguide/gradle_wrapper.html) podemos ejecutar el programa en cualquier maquina y cualquier OS, sin necesidad de tener instalado **Gradle** .

#### Instalacion y primer acercamiento con Apache Spark

El siguiente paso era obviamente, instalar **Apache Spark** y aprender a utilizarlo. Por suerte, con [este articulo](https://sparkbyexamples.com/spark/spark-installation-on-linux-ubuntu/) fue bastante simple.

Ahora, sobre como utilizar/implementar **Apache Spark** en el proyecto estuve bastante perdido durante varios dias, asi que directamente decidi rehacer algunas partes de lo ya hecho en el laboratorio 2, para poder volverme mas cercano a **Gradle** y empezar con envion una vez sepa como incluir el framework en el proyecto.

Luego de una larga busqueda, me tope con [esta playlist](https://www.youtube.com/playlist?list=PLQDzPczdXrTgqEc0uomGYDS0SFu7qY3g3). Este es un mini curso de **Apache Spark** hecho por [Rishi Srivastava](https://www.youtube.com/@backstreetbrogrammer) y me ayudo bastante con entender el para que y como utilizar el framework.

Eso si, por alguna razon, al ejecutar el programa ya utlizando algunas funciones de Spark, mostraba el siguiente error:

```bash
class org.apache.spark.storage.StorageUtils$ (in
unnamed module @0x41a962cf) cannot access class sun.nio.ch.DirectBuffer (in
module java.base) because module java.base does not export
sun.nio.ch<http://sun.nio.ch> to unnamed module @0x41a962cf
```

Pase un tiempo buscando hasta encontrar [esta pregunta](https://www.mail-archive.com/search?l=user@spark.apache.org&q=subject:%22Re%5C%3A+%5C%5BJava+17%5C%5D+%5C-%5C-add%5C-exports+required%5C%3F%22&o=newest&f=1) en un foro, que basicamente habla sobre incluir unos argumentos al ejecutar `javac`, y para ello debia ver tambien como decirle a gradle que incluya estos argumentos a la hora de ejecutar `run`. Por suerte divagando un poco en la documentacion de **Gradle** encontre [como hacerlo](https://docs.gradle.org/current/userguide/application_plugin.html) (por alguna razon el antivirus me bloquea la pagina, pero antes no lo hacia).

#### Integrando Apache Spark en el programa

Por ultimo, a la hora de integrar **Apache Spark** en el programa no fue tan dificil, lo primero fue guardar los `Feeds` que estaban en memoria en la estructura **RDD** de **Spark** para poder acceder a la informacion directamente utilizando las acciones y transformaciones que el framework nos provee. Esto fue para poder mostrar un feed especifico a eleccion del usuario.

Tambien cree otra estructura **RDD** que contiene todos los articulos, para facilitar la busqueda por palabras claves y el conteo de entidades nombradas.

### Preguntas

#### ¿Cómo se instala Spark en una computadora personal?

Los pasos para instalar Spark en una PC son los siguientes:

1. Descargar el archivo comprimido desde la [pagina de descargas](https://spark.apache.org/downloads.html)
2. Descomprimimos el archivo y movemos sus contenidos a una carpeta llamada "spark"

```bash
$ tar -xzf spark-{version}-bin-hadoop2.7.tgz
$ mv spark-{version}-bin-hadoop2.7 spark
```

3. Seteamos las variables de entorno, este paso puede diferir dependiendo del sistema operativo, en linux hacemos lo siguiente: - Abrimos el archivo .bashrc con algun editor, yo utilice nano.
   `bash
$ nano ~/.bashrc 
` - Añadimos las siguientes lineas al final
   `bash
export SPARK_HOME=/home/{usuario}/spark
export PATH=$PATH:$SPARK_HOME/bin
`
   Esto hara que cada vez que iniciemos la terminal, `PATH` tenga una referencia a las librerias de **Spark**.

Con esto ya estaria instalado **Apache Spark** y se puede comprobar ejecutando

```bash
$ spark-shell
```

Sources:

- [Como instalar Apache Spark en Ubuntu](https://sparkbyexamples.com/spark/spark-installation-on-linux-ubuntu/)

#### ¿Qué estructura tiene un programa en Spark?

Principalmente, la estructura de un programa en **Spark** siempre va a tener al principio un objeto del tipo `SparkContext` el cual representa una coneccion con el cluster, de "workers".

A la hora de crear este `SparkContext`, debemos pasarle una configuracion, esta puede tener cosas como el nombre de la aplicacion, o la cantidad de hilos trabajadores con los que se ejecutaran las tareas. En Java esto se hace de la siguiente manera:

```Java
static private final SparkConf SPARK_CONF = new SparkConf()
                                                    .setAppName("Nombre")
                                                    .setMaster("local[*]");
final var sparkContext = new JavaSparkContext(SPARK_CONF)
```

En este caso `local[*]` le especifica que la cantidad de hilos trabajadores es igual a la cantidad de cores del procesador.

Luego, podemos guardar cualquier tipo de documentos, ya sean archivos locales o informacion en memoria, pasandole alguna lista a la clase `JavaRDD` y asi podremos acceder a sus metodos.
Por ejemplo, guardamos una lista con enteros del 1 al 5:

```Java
List<Integer> data = Stream.iterate(1, n -> n + 1)
                           .limit(5)
                           .collect(Collectors.toList());
JavaRDD<Integer> myRDD = sparkContext.parallellize(data);
```

O el siguiente codigo, que guarda cada linea de un archivo como un elemento del RDD:

```Java
JavaRDD<String> myRDD = sparkContext.textFile("Path/To/File.txt");
```

Al ultimo debemos cerrar el sparkContext para evitar "resource leaking" o podemos encerrar su declaracion el un bloque `try` como se hace en nuestro programa.

Sources:

- [SparkContext](https://spark.apache.org/docs/3.2.0/api/java/org/apache/spark/SparkContext.html#:~:text=A%20SparkContext%20represents%20the%20connection,before%20creating%20a%20new%20one.)
- [Metodo setMaster](https://stackoverflow.com/questions/32356143/what-does-setmaster-local-mean-in-spark)
- [Clase 18 - Creando un RDD en Spark](https://www.youtube.com/watch?v=H2a34mu_8wk&list=PLQDzPczdXrTgqEc0uomGYDS0SFu7qY3g3&index=19)
- [Clase 27 - Creando un RDD en Spark con data externa](https://www.youtube.com/watch?v=F5SaCBDia-I&list=PLQDzPczdXrTgqEc0uomGYDS0SFu7qY3g3&index=27)

#### ¿Qué estructura tiene un programa de conteo de palabras en diferentes documentos en Spark?

Para contar instancias de palabras en un documento, primero tendremos una variable `data` la cual sera un objecto `JavaRDD<String>` Y nos aseguraremos de que cada elemento sea una palabra, puede ser utilizando `.split(" ")` o como se prefiera, en este caso, utilizaremos el metodo `flatMap()`:

```Java
JavaRDD<String> inputFile = sparkContext.textFile(fileName);
JavaRDD<String> wordsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split(" ")));
```

Luego utilizamos `.mapToPair()` para crear una tupla por cada palabra con la siguiente forma (palabra, cantidad), en un principio "cantidad" sera uno, y para aumentar ese numero cada vez que "palabra" se repita, llamamos a ".reduceByKey(Integer::sum)" para sumar las cantidades de cada palabra especifica.

```Java
JavaPairRDD countData = wordsFromFile.mapToPair(t -> new Tuple2(t, 1)).reduceByKey(Integer::sum);
```

Por ultimo, si queremos mostrar por pantalla:

```Java
countData.foreach(tuple -> {
    System.out.println(tuple._1() + " :: " + tuple._2());
});
```

Sources:

- [Ejemplo de conteo de palabras con Apache Spark en Java](https://www.digitalocean.com/community/tutorials/apache-spark-example-word-count-program-java)

#### ¿Cómo adaptar el código del Laboratorio 2 a la estructura del programa objetivo en Spark?

La adaptacion del codigo del Laboratorio 2 a la estructura de Spark fue mas que nada cambios al main (En mi caso, pase toda la logica a una clase extra "FeedRaderHandler").
Lo primero fue, hacer que las clases de `Feed`, `Article` y `NamedEntity` implementaran la interfaz `Serializable`, esto nos permite "serializar" nuestro objeto a un "byte stream", para que asi pueda ser revertido a una copia del mismo.

Y luego pasar toda la data obtenida de los parsers a objetos **RDD**, lo cual facilita mucho la adicion de interactividad del programa y el conteo de entidades nombradas.

Sources:

- [Serialization Exception in Apache Spark and Java](https://stackoverflow.com/questions/73017578/serialization-exception-in-apache-spark-and-java)

#### ¿Cómo se integra una estructura orientada a objetos con la estructura funcional de map-reduce?

A la hora de integrar una estructura orientada a objectos con uan estructura funcional de map-reduce, lo mas importante es definir como se mapean las unidades de procesamiento relevantes de la estructura de objetos. La paralelizacion de una lista de `Feed` y el mapeo de los articulos a estructuras de tipo `JavaRDD` son claros ejemplos de esto.

Por otro lado, incluir el mapeo de objetos en pares clave-valor, como hacemos a la hora de contar las entidades nombradas, es importante, pues esta es otra caracteristica de este tipo de estructura funcional.
