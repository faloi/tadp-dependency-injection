tadp-dependency-injection
=========================

El proyecto está mavenizado (https://sites.google.com/site/programacionui/material/herramientas/java/maven) así que para instalarlo tienen que hacer algunas cositas. Les pongo la versión gráfica y la versión consolera de hacerlo:

Versión GUI (mediante el plugin de eclipse)
-------------------------------------------

- Abren el Eclipse, van a **Help->Install new software...** y donde dice **Work with** pegan esta url http://download.eclipse.org/technology/m2e/releases y le dan Enter. Esperan un rato y en la listita les va a aparecer el plugin y lo descargan. NOTA: no estoy seguro de que el plugin baje también el maven, si ven que les da algún error loco bajensé el maven (en Linux `sudo apt-get install maven2`, en Windows debe haber algún instalador loco).

- Con el plugin instalado, van a **File->Import...->Existing Maven Projects** y eligen la carpeta tadp-dependency-injection (o como sea que la hayan llamado).

- Para comprobar que haya quedado funcionando pueden probar de correr los tests (esto va a bajar las dependencias, compilar y finalmente correrlos), para esto hacen clic derecho sobre el proyecto **Run As->Maven test**. Va a tardar un rato porque va a bajar un montón de cosas que necesita el maven para funcionar, pero si todo sale bien deberían ver por la consola que pasaron los tests.


Versión consolera
-----------------

```
sudo apt-get install maven2
cd tadp-dependency-injection
mvn test
mvn eclipse:eclipse
```

Si todo salió bien, el ultimo comando les debería haber generado los archivos que necesita el Eclipse, con lo que podrían importar el proyecto desde **File->Import...->Existing Projects into Workspace**.
