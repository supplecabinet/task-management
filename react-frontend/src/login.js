import React, { useState } from 'react';
import axios from 'axios';
import App from './App';
import ReactDOM from 'react-dom';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

  const Login = (props) => {
  const [userName, setUserName] = useState('')
  const [password, setPassword] = useState('')
  const [userNameError, setUserNameError] = useState('')
  const [passwordError, setPasswordError] = useState('')



  const onButtonClick = () => {
    // Set initial error values to empty
    setUserNameError('')
    setPasswordError('')
  
    // Check if the user has entered both fields correctly
    if ('' === userName) {
      setUserNameError('Please enter your username')
      return
    }
  
    if ('' === password) {
      setPasswordError('Please enter a password')
      return
    }
  
    if (password.length < 7) {
      setPasswordError('The password must be 8 characters or longer')
      return
    }
    //Login API call
    const loginData  ={username: userName, password: Buffer.from(password).toString('base64')};
    fetch('http://localhost:8080/api/v1/auth/login', {
      method: 'POST',
      body: JSON.stringify(loginData),
      headers: {
        'Content-Type': 'application/json'
      }
    }).then( res => {
      if (!res.ok) {
        toast.error("Bad Credentials!");
        return;
      }
      sessionStorage.setItem("X-AUTH-TOKEN", res.headers.get("x-auth-token"));
          setTimeout(() => {
            
            ReactDOM.render(
              <React.StrictMode>
                <App />
              </React.StrictMode>,
              document.getElementById('root')
            );
          }, 200);
       
        //Set Auth Token in session storage
        
         
    });
    
  }
  return (
    <div className={'mainContainer'}>
      <div className={'titleContainer'}>
        <div>Login</div>
      </div>
      <br />
      <div className={'inputContainer'}>
        <input
          value={userName}
          placeholder="Enter your username here"
          onChange={(ev) => setUserName(ev.target.value)}
          className={'inputBox'}
        />
        <label className="errorLabel">{userNameError}</label>
      </div>
      <br />
      <div className={'inputContainer'}>
        <input 
          type="password"
          value={password}
          placeholder="Enter your password here"
          onChange={(ev) => setPassword(ev.target.value)}
          className={'inputBox'}
        />
        <label className="errorLabel">{passwordError}</label>
      </div>
      <br />
      <div className={'inputContainer'}>
        <input className={'inputButton'} type="button" onClick={onButtonClick} value={'Log in'} />
      </div>
      <div>
      <ToastContainer position="bottom-right" />
      </div>
    </div>
  )
}

export default Login