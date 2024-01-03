# Informe Individual

## Estructura del proyecto

- `src`: Codigo fuente del proyecto
- `lib`: Directorio con las diferentes librerias
- `bin`: Las clases compiladas se guardan aqui por defecto.
- `.vscode/settings.json`: Indica las dependencias del proyecto 
- `config/subscriptions.json`: Indica los feeds a obtener


Elegi utilizar proyectos de referencia en github, me guié mucho por los ejemplos del [repositorio de la libreria](https://github.com/apache/spark/blob/master/examples/src/main/java/org/apache/spark/examples) y [este ejemplo](https://javadeveloperzone.com/spark/spark-wordcount-example/).


## Preguntas
### **¿Cómo se instala Spark en una computadora personal?**

En mi caso personal cree un proyecto java en vscode, me parece mas sencillo que usar Maven o Gradle que me trajeron mil problemas. Instalar una libreria como spark es tan sencillo como descargar los `.jars` de la libreria y referenciarlos en la pestaña *JAVA PROJECT/Referenced Libraries*.


### **¿Qué estructura tiene un programa en Spark?**

Un programa en Spark utiliza objetos inmutables llamados `RDD` (Resilient Distributed Datasets). Estos son colecciones de elementos particionados de tal manera que se puedan operar en paralelo, por lo general se utilizan funciones del ***map*** y ***reduce*** para a partir de un RDD conseguir otros. Esto es clave porque queremos partir de Articulos y llevarlos a Strings.  

Lo primero que necesita un programa en Spark es un `SparkContext` que da acceso a las distintas funcionalidades del programa, como crear los RDD. 
Primero creamos un objeto de `SparkSession` que conteniene la información del programa.
Por ejemplo, en WordCount.java del repositorio de guia tenemos:
```java
SparkSession spark = SparkSession.builder()
        .appName("")
        .master("local[*]")
        .getOrCreate();
```
Luego creamos el `SparkContext` a partir del SparkSession.
```java
JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());
```
El repositorio que tomo como referencia crea un RDD a partir de un archivo de texto
```java
JavaRDD<String> lines = spark.read().textFile(args[0]).javaRDD();
```
Pero podemos obtenerlos **Paralelizando** alguna colección, por ejemplo:
Supongamos que tenemos una lista llamada *listOfNames*

```java
JavaRDD<String> = sc.parallelize(listOfNames);
```

Luego podemos crear nuevos RDD a partir del primer RDD obtenido.

### **¿Qué estructura tiene un programa de conteo de palabras en diferentes documentos en Spark?**

Siguiendo el ejemplo del repositorio podemos hacer algo del estilo: 

```java
private static final Pattern SPACE = Pattern.compile(" ");
JavaRDD<String> words = lines.flatMap(s -> Arrays.asList(SPACE.split(s)).iterator());
```
Esto separa las palabras de un arreglo y devuelve un RDD con todas las palabras encontradas. El metodo `iterator()` nos permite iterar sobre una colección 

```java
JavaPairRDD<String, Integer> ones = words.mapToPair(s -> new Tuple2<>(s, 1));
```
Aqui se usa el metodo `mapToPair` para mapear cada palabra a una tupla con ella misma y su frecuencia, que seteamos en 1.

```java
JavaPairRDD<String, Integer> counts = ones.reduceByKey((i1, i2) -> i1 + i2);
```

Podemos utilizar el metodo `reduceByKey`, esto es una transformación que se aplica al valor de cada clave, en esta caso las frecuencias.

### **¿Cómo adaptar el código del Laboratorio 2 a la estructura del programa objetivo en Spark?**

Lo principal es hacer que las clases participantes deben implementar la interfaz `Serializable`.
La serialización se refiere a convertir los objetos en un `stream de bytes` y vice-versa, esto es una forma optima de transferirlos entre nodos, o para guardarlos en un buffer de memoria.

Luego para paralelizar el conteo de entidades debemos *mapear* cada Articulo a su lista de entidades nombradas, esto se puede hacer facilmente adaptando el código de referencia. 

```java
JavaRDD<Article> articleRDD = sc.parallelize(allArticles);

JavaRDD<NamedEntity> namedEntitiesRDD = articleRDD.flatMap(article -> {
    return article
        .computeNamedEntities(article, heuristic)
        .getNamedEntityList()
        .iterator();
});		
```
A partir de esto podemos obtener tanto los nombres como las categorias. 

### **¿Cómo se integra una estructura orientada a objetos con la estructura funcional de map-reduce?**

Es importante que las clases a implementar permitan aplicar los metodos necesarios, por ejemplo, si queremos usar `mapToPair` debemos hacerlo sobre una tupla o implementar la interfaz `Serializable`. Además de tener que devolver los tipos correctos.