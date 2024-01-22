const {createApp} = Vue

let app = createApp({
    data(){
        return {
            email : "",
            emailReg : "",
            firstName : "",
            lastName : "",
            passwordReg : "",
            password : "",
            error : "",
            errorReg : "",
        }
    },
    created(){
        console.log(this.registered)

    },

    methods: {
        login(email, password){
            if(!email || !password){
                this.error = "Please fill in all fields";
                return;
            }
            axios.post("/api/login?email=" + email + "&password=" + password)
            .then(response => {
                console.log(response)

                if (email.includes("@adminanb.com")) {
                    window.location.href = "/web/admin/managerOverview.html";
                } else {
                    window.location.href = "/web/pages/accounts.html";
                }
    
                this.clearData();
            })
            .catch(error => {
            this.error = error.response.data,
            console.log(error)
            })
        },
        loginEvent(){
            this.login(this.email, this.password)
        },
        validatePassword(password) {
            const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
      
            return passwordRegex.test(password);
          },
      
        register(){
            if(!this.firstName || !this.lastName || !this.emailReg || !this.passwordReg){
                this.errorReg = "Please fill in all fields";
                return;
            }

            if (!this.validatePassword(this.passwordReg)) {
                this.error = "Password must have at least 8 characters, one uppercase letter, one lowercase letter, one number, and one special character.";
                return;
              }
            axios.post('/api/clients',{
                "firstName" : this.firstName,
                "lastName" : this.lastName,
                "email": this.emailReg,
                "password": this.passwordReg  
            })
            .then(response => {
                Swal.fire({
                    title: "Success",
                    text: "Registration completed successfully",
                    icon: "success"
                });

                setTimeout(() => {
                    this.login(this.emailReg, this.passwordReg)
                }, 2000);

            })
            .catch(
                error => {
                console.log(error)
                this.errorReg = error.response.data
            })
        },
        clearData(){
            this.email = ""
            this.firstName = ""
            this.lastName = ""
            this.password = ""
        }

        
    }
}).mount('#app')
