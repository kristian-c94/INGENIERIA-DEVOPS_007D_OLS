# INGENIERIA-DEVOPS_007D_OLS
Aprendiendo practicas Devops 
# PRUEBA 1 INGENIERIA DEVOPS
## Inicialización de repositorio
Se inicializo el repositorio en el directorio del proyecto, para después realizar el primer commit del proyecto que seria la subida del mismo en su totalidad
## Creacion de branches (ramas)
Se realizo la creacion de 3 tipos de ramas para asegurar un buen entorno de desarrollo devops.
Con los siguientes comandos:
git checkout -b developer
git checkout -b features/agregar-logs <-- este servira para añadir logs de los microservicios
git checkout -b hotfix/corregir-puerto <--- en caso de que alguna de los puertos este mal declarado y poder solucionarlo de forma rapida
### Developer
En esta rama se guarda el código con los cambios realizadas en las otras ramas, este seria como la rama que guarda el código con modificaciones para después ser aprobado y ser implementado en el main que seria la aplicación principal o que se muestra al publico

Se crea la rama develop y se actualiza
-git checkout -b develop
-git push -u origin develop

(dentro de ella estaran las otras dos ramas) 

### Features 
Aca se estaran guardando las nuevas implementaciones que se le puedan añadir al codigo

Se crea la rama dentro de developer
git checkout -b feature/agregar-logs

Se crea un documento 
echo "// Sistema de logs" > logs.txt

Se añade la solicitud para despues realizar un pull request
git add .
git commit -m "feat: agregar sistema de logs al microservicio"
git push -u origin feature/agregar-logs

### Hotfix
En esta rama principalmente se utilizara para parches que deban ser añadidos con rapidez para solucionar un problema 

Se abre la rama main y se actualiza
git checkout main
git pull origin main

Se crea la rama dentrod de main
git checkout -b hotfix/corregir-puerto

Se crea documento de prueba 
echo "# Correccion de puerto" >> config.txt

Se envia para despues hacer el respectivo pull request 
git add .
git commit -m "fix: corregir puerto de conexion en produccion"
git push -u origin hotfix/corregir-puerto
