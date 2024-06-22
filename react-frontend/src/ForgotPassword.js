import React, { useState } from 'react';
import './LoginSignUp.css';
import ReactDOM from 'react-dom';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import user_icon from './assets/person.png';
import pwd_icon from './assets/password.png';
import LoginSignUp from './LoginSignUp';

const ForgotPassword = () => {
  const [action,setAction] = useState("Send OTP");
  const [userName, setUserName] = useState('');
  const [password, setPassword] = useState('');
  const [password1, setPassword1] = useState('');
  const [password2, setPassword2] = useState('');
  const TASKS_BASE_URL = "http://localhost:8080/api/v1/";

  const redirectLogin = () => {
    setTimeout(() => {
      ReactDOM.render(
        <React.StrictMode>
          <LoginSignUp />
        </React.StrictMode>,
        document.getElementById('root')
      );
    }, 200);
  }
  const onVerify = () => {
    if (password1 !== password2) {
      toast.warn("Passwords are not matching!");
      return;
    }
    if (password1 === "" || password2 === "") {
      toast.warn("Please enter passwords!");
      return;
    }
    if (password === "") {
      toast.warn("OTP is missing!");
      return;
    }
    const validateData  = {otp: password, password: Buffer.from(password1).toString('Base64')};
    fetch(`${TASKS_BASE_URL}validate/otp`, {
      method: 'POST',
      body: JSON.stringify(validateData),
      headers: {
        'Content-Type': 'application/json'
      }
    }).then( res => {
      if (!res.ok) {
        toast.error("Something went wrong!");
        return;
      } else {
        toast.success("Password changed Successfully!");
        setTimeout(() => {
          ReactDOM.render(
            <React.StrictMode>
              <LoginSignUp />
            </React.StrictMode>,
            document.getElementById('root')
          );
        }, 3000);
      }
    });
  }
  const onSubmit = () => {
   
    if (action !== "Send OTP") {
      return;
    }
    const otpData  = Buffer.from(userName).toString('Base64');
    fetch(`${TASKS_BASE_URL}request/otp`, {
      method: 'POST',
      body: otpData,
      headers: {
        'Content-Type': 'text/plain'
      }
    }).then( res => {
      if (!res.ok) {
        toast.error("Invalid username or OTP already sent!");
        return;
      } else {
        toast.success("OTP sent to registered Email!");
        setAction("Verify");
        return;
      }
    });
    
  }
  return (
  <div className="container">
    <div className='header'>
      <div className='text'>Forgot Password</div>
      <div className='underline'></div>
    </div>
    <div className="inputs">
      <div className="input">
        <img src={user_icon} alt=""/>
        <input type="text" disabled = {action === "Verify"? true: false} value={userName} onChange={(ev) => setUserName(ev.target.value)} placeholder='User Name'/>
      </div>
      {action==="Verify"? <div className="input"><img src={pwd_icon} alt=""/><input type="password" value={password} onChange={(ev) => setPassword(ev.target.value)} placeholder='OTP'/></div>:""}
      {action==="Verify"? <div className="input"><img src={pwd_icon} alt=""/><input type="password" value={password1} onChange={(ev) => setPassword1(ev.target.value)} placeholder='Password'/></div>:""}
      {action==="Verify"?<div className="input"><img src={pwd_icon} alt=""/><input type="password" value={password2} onChange={(ev) => setPassword2(ev.target.value)} placeholder='Confirm Password'/></div>:""}
      </div>
      <div className="forgot-password">Login? <span onClick={()=>{redirectLogin()}}>Click Here!</span></div>:<div></div>
      <div className='submit-container'>
        <div className={action==="Send OTP"?"submit gray":"submit"} onClick={()=>{ if (action ==="Verify") {onVerify()}}}>Verify</div>
        <div className={action==="Verify"?"submit gray":"submit"} onClick={()=>{if (action ==="Send OTP") {onSubmit()}}}>Send OTP</div>
      </div>
      <div>
      <ToastContainer position="bottom-right" />
      </div>
  </div>
  )
}

export default ForgotPassword;