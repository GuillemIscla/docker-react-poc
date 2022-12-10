# Docker react POC
This pr shows how we can have in the same project a scala application and several
webapps being developed. It builds into a docker image that contains the webapps 
already built. To develop them, a script builds the webapps and places into the 
resources of the app.

## The project
In the server folder, the scala app is been built.

In the webapps folder, any number of webapps are built, each one in its folder. 
To develop them I used react with typescript but many other frameworks 
can be used with this same structure.

The .gitignore omits the folders in the resources where the webapps build files 
are placed. The same for the build folders in each webapp.

## Developing the backend
When the backend developer wants a fresh version of the webapps built in his 
local, he can run the script:
```aidl
sh deployment/LocalDeployment.sh
```
This script builds all the webapps and places them into the resources; with a 
parameter the script builds only the specified webapps.

Once built, the entrypoint at the view controller delivers the webapp (one 
entrypoint needs to be added for each webapp) and assets that require 
(images, css, js files...) are delivered through the the same `assets` endpoint. 
For the paths to match, the webapps need to have the `homepage` field in the 
`package.json` pointing into: `assets/webapps/{name_of_the_webapp}/`

## Developing the frontend
If the frontend developer wants to have a running server to hit the REST api, he
just needs to run (when having sbt installed...):
```aidl
sbt run
```

## Building the app
The webapps list needs to be updated in the build.sbt file if any is added.

Other than that it relies on a multi-build with three images:
1) An image with npm that copies the webapps files and build each of them
2) An image with jdk that copies the files built in the first image and updates 
the main jar with them
3) An image with jre that copies all the files and runs the app

Other sbt considerations are:

- The necessary files to build the webapp need to be added in the Docker mappings
in order for the Dockerfile to be able to copy them.
- The build files of the webapp in the resources that helped with local development 
need to be excluded from the compile mappings (NOTE: These files were already 
not tracked by git).

