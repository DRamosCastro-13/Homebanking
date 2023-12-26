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
        login(){
            axios.post("/api/login?email=" + this.email + "&password=" + this.password)
            .then(response => {
                console.log(response)
                window.location.href = "/web/pages/accounts.html"
            })
            .catch(error => console.log(error))
        },
        register(){
            axios.post("/api/clients?firstName=" + this.firstName + "&lastName=" + this.lastName + "&email=" + this.emailReg + "&password=" + this.passwordReg)
            .then(response => {
                console.log(response)
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
