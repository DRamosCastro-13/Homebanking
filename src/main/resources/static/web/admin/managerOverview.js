const {createApp} = Vue

let app = createApp({
    data(){
        return {
            data: [],
            client: [],
            firstName : "",
            lastName : "",
            email : "",
            accounts : [],
            availableLoans : [],
            loans : [],
            error : ""
        }
    },
    created(){
        this.loadData()
        this.loadLoan()
    },

    methods: {
        loadData(){
            axios('/api/clients/current/admin')
            .then(response =>{ 
                this.data = response,
                this.client = response.data,
                console.log(this.client)
            })
            .catch(error => {
                this.error = error.response.data
                console.log(error)
            })
        },
        loadLoan(){
            axios('/api/loans/availableLoans')
            .then(response =>{
                this.data = response,
                this.availableLoans = response.data,
                console.log(this.availableLoans)
            })
            .catch(error => console.log(error))
        },
        logOut(){
            axios('/api/logout')
            .then(
                window.location.href = "../index.html"
            )
        }

    }
}).mount('#app')

