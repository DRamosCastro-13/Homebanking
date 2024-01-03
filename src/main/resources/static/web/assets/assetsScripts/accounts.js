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
            loans : [],
        }
    },
    created(){
        this.loadData()
        
    },

    methods: {
        loadData(){
            axios('/api/clients/current')
            .then(response =>{ 
                this.data = response,
                this.client = response.data,
                this.accounts = response.data.accounts.sort((a,b) => a.id - b.id),
                this.loans = response.data.loans,
                console.log(this.loans)
            })
            .catch(error => console.log(error))
        },
        logOut(){
            axios('/api/logout')
            .then(
                window.location.href = "../index.html"
            )
        },
        createAccount(){
            axios.post('/api/accounts/clients/current?')
            .then(response => {
                console.log(response)
                window.location.href="../pages/accounts.html"
            })
        }
    }
}).mount('#app')

