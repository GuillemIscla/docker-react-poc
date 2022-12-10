import { useState } from 'react';
import './App.css';

function ShowPage()  {

    const [message, setMessage] = useState<string>("")

    return (
      <div className="container wrapper">
        <div id="top">
            <h1>Welcome to the Docker-React-Poc</h1>
            <p>This is the react-app-2</p>
        </div>
        <div className="wrapper">
            <div id="main">
                <h1>This page has been created with react</h1>
                <p>The react code is in the same project as the server. You can also build locally
                the webapps when you are developing and the build files excluded in the .gitignore file.
                <br></br>Its dockerized in a multi-stage build and the build files for the webapp are copied into
                the resources of the app during the process</p>
                <p>And of course, you can communicate with the server (click the button)</p>
                <button
                  className="btn btn-outline-success my-2 my-sm-0"
                  onClick={_ => fetch("../message/2").then(res => res.text()).then(setMessage)}
                >Get Message</button>
                <div>{message}</div>
            </div>
        </div>
        <hr></hr>
        <div className="bottom" style={{color: "#4CAF50"}}><i>Created by Guillem Iscla</i></div>

      </div>);
}

export default ShowPage;
