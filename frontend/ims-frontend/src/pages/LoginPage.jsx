import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import ApiService from "../service/ApiService";

const LoginPage = () => {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");

    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const loginData = { email, password };

            const res = await ApiService.loginUser(loginData);

            console.log(res);

            if (res.status === 200) {

                ApiService.saveToken(res.token);
                ApiService.saveRole(res.role);

                showMessage(res.message);

                navigate("/dashboard");
            }

        } catch (error) {

            showMessage(
                error.response?.data?.message ||
                "Error occurred during login"
            );

            console.log(error);
        }
    };


    const showMessage = (msg) => {
        setMessage(msg);

        setTimeout(() => {
            setMessage("");
        }, 4000);
    };


    return (
        <div className="auth-container">

            <h2>Login</h2>

            {message && <p className="message">{message}</p>}

            <form onSubmit={handleLogin}>

                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />

                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />

                <button type="submit">Login</button>

            </form>

            <p>
                Don't have an account? <Link to="/register">Register</Link>
            </p>

        </div>
    );
};

export default LoginPage;