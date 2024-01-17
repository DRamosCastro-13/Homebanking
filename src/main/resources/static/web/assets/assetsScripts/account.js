const {createApp} = Vue

let app = createApp({
    data(){
        return {
            data: [],
            account: [],
            firstName : "",
            lastName : "",
            email : "",
            accounts : [],
            id: 1,
            transactions: []
        }
    },
    created(){
        this.id = new URLSearchParams(window.location.search).get('id')
        this.loadData()
    },

    methods: {
        loadData(){
            const url = 
            axios.get('/api/clients/current')
            .then(response =>{ 
                this.data = response.data,
                this.accounts = this.data.accounts,
                this.account = this.accounts.find(account => account.id == this.id),
                this.transactions = this.account.transactions.sort((a,b) => b.id - a.id),
                console.log(this.transactions)
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