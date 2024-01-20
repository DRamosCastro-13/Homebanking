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
        }
    },
    created(){
        console.log(this.registered)

    },

    methods: {
        login(email, password){
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
        errorAlert(error){
            Swal.fire({
                icon: "error",
                title: error.response.data,
                text: "Please try again",
              })
        },
        register(){
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
                this.error = error.response.data
                Swal.fire({
                    icon: "error",
                    title: error.response.data,
                    text: "Please try again",
                })
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
