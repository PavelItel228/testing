import React from 'react';

export default class Form extends React.Component {

    state = {
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        nameError: '',
        emailError: '',
        passwordError: '',
        confirmedPasswordError: '',
        hasErrors: true
    };

    isNumeric = n => {
        return !isNaN(parseFloat(n)) && isFinite(n);
    }

    validate = (callback) => {
        let nameError = "";
        let emailError = "";
        let passwordError = "";
        let confirmedPasswordError = "";
        let hasErrors;
        const re = /^(([^<>()\[\].,;:\s@"]+(\.[^<>()\[\].,;:\s@"]+)*)|(".+"))@(([^<>()[\].,;:\s@"]+\.)+[^<>()[\].,;:\s@"]{2,})$/i;
        console.log(this.state.password.length)
        if(this.isNumeric(this.state.password)){
            passwordError = "password must contain letters";
        }
        if(this.state.password.length < 8) {
            passwordError = "password must contain at least 8 character's";
        }
        if(!re.test(this.state.email.toLowerCase())){
            emailError = "invalid email"
        }
        if(this.state.password !== this.state.confirmPassword){
            confirmedPasswordError = "password dont match"
        }
        if(emailError || nameError || passwordError || confirmedPasswordError) {
            hasErrors = false;
            this.setState({emailError, nameError, passwordError, confirmedPasswordError}, callback(hasErrors));
            return;
        }
        hasErrors  = true;
        this.setState({emailError, nameError, passwordError, confirmedPasswordError}, callback(hasErrors));
    }

    handleChange = (event) => {
        this.setState({
            [event.target.name] : event.target.value},
            () => {
            console.log(this.state)
            this.validate((hasErrors) => {
                if (hasErrors){
                    this.setState({hasErrors: false});
                } else {
                    this.setState({hasErrors: true});
                }
            });
        });
    }

    render() {
        return (
            <div id="app" className="login-form">
                <form action="/accounts/registration" method="post">
                    <h2 className="text-center">Registration</h2>
                    <div className="form-group">
                        <input name="username" id="username" type="text"
                               className={'form-control ' + ((this.state.username === "")? "" : ((this.state.nameError)?"is-invalid" : "is-valid"))}
                               placeholder="username"
                               required="required"
                               value={this.state.username}
                                onChange={this.handleChange}>
                        </input>
                        <span className="error">{this.state.nameError}</span>
                    </div>
                    <div className="form-group">
                        <input name="email" id="email" type="email"
                               className={'form-control ' + ((this.state.email === "")? "" : ((this.state.emailError)?"is-invalid" : "is-valid"))}
                               placeholder="email"
                               required="required"
                               value={this.state.email}
                               onChange={this.handleChange}>
                        </input>
                        <span className="error">{this.state.emailError}</span>
                    </div>
                    <div className="form-group">
                        <input type="password" name="password" id="password"
                               className={'form-control ' + ((this.state.password === "")? "" : ((this.state.passwordError)?"is-invalid" : "is-valid"))}
                               placeholder="password"
                               value={this.state.password}
                               onChange={this.handleChange}
                               required="required">
                        </input>
                        <span className="error">{this.state.passwordError}</span>
                    </div>
                    <div className="form-group">
                        <input type="password" name="confirmPassword" id="confirmPassword"
                               className={'form-control ' + ((this.state.confirmPassword === "")
                                   ? "" : ((this.state.confirmationPasswordError)?"is-invalid" : "is-valid"))}
                               placeholder="confirmPassword"
                               value={this.state.confirmPassword}
                               onChange={this.handleChange}
                               required="required">
                        </input>
                        <span className="error">{this.state.confirmedPasswordError}</span>
                    </div>
                    <div className="form-group">
                        <button type="submit" className="btn btn-primary btn-block" disabled = {this.state.hasErrors}>Sing up</button>
                    </div>
                </form>
                <p className="text-center">
                    <a href="/accounts/login"> i already have an account</a>
                </p>
                <ul id="lang">
                    <li><a className="underlineHover" href="?lang=en">EN</a></li>
                    <li><a className="underlineHover" href="?lang=ua">UA</a></li>
                </ul>
            </div>
        );
    }
}