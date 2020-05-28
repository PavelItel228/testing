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

    validate = () => {
        let nameError = "";
        let emailError = "";
        let passwordError = "";
        let confirmPasswordError = "";

        if(this.state.password.length <= 8) {
            passwordError = "password must contain at least 8 character's";
        }
        if(emailError || nameError || passwordError || confirmPasswordError) {
            this.setState({emailError, nameError, passwordError, confirmPasswordError});
            return false;
        }
        this.setState({emailError, nameError, passwordError, confirmPasswordError});
        return true;
    }

    handleChange = (event) => {
        const isCheckbox = event.target.type === "checkbox";
        this.setState({
            [event.target.name]: isCheckbox
                ? event.target.checked
                : event.target.value
        });
        const isValid = this.validate();
        if (isValid){
            this.setState({hasErrors: false});
            console.log(this.state);
        } else {
            this.setState({hasErrors: true});
        }
    }

    render() {
        return (
            <div id="app" className="login-form">
                <form action="/accounts/registration" method="post">
                    <h2 className="text-center">Registration</h2>
                    <div className="form-group">
                        <input name="username" id="username" type="text" className="form-control"
                               placeholder="username"
                               required="required"
                                onChange={this.handleChange}>
                        </input>
                        <span className="error">{this.state.nameError}</span>
                    </div>
                    <div className="form-group">
                        <input name="email" id="email" type="email" className="form-control"
                               placeholder="email"
                               required="required"
                               onChange={this.handleChange}>
                        </input>
                        <span className="error">{this.state.emailError}</span>
                    </div>
                    <div className="form-group">
                        <input type="password" name="password" id="password" className="form-control"
                               placeholder="password"
                               value={this.state.password}
                               onChange={this.handleChange}
                               required="required">
                        </input>
                        <span className="error">{this.state.passwordError}</span>
                    </div>
                    <div className="form-group">
                        <input type="password" name="confirmPassword" id="confirmPassword" className="form-control"
                               placeholder="confirmPassword"
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