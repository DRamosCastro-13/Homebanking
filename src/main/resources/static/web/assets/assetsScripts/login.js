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
                window.location.href = "/web/pages/accounts.html"
            })
            .catch(error => console.log(error))
        },
        loginEvent(){
            this.login(this.email, this.password)
        },
        register(){
            axios.post("/api/clients?firstName=" + this.firstName + "&lastName=" + this.lastName + "&email=" + this.emailReg + "&password=" + this.passwordReg)
            .then(response => {
                console.log(response)
                this.login(this.emailReg, this.passwordReg)
                this.clearData()
            })
            .catch(error => {
                console.log(error)
                this.error = error.response.data
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
