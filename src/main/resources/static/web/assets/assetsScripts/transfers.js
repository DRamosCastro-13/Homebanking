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
            amount : '',
            description : '',
            originAccount : '',
            targetAccount : '',
            transferType: '',
            selectedAccount: '',
            error : ""
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
                this.accounts = response.data.accounts
                console.log(this.accounts)
            })
            .catch(error => console.log(error))
        },
        getBalance(account){
            axios('/api/clients/current')
            .then(response => {
                selectedAccount = this.accounts.find(item => item.id == account)
                console.log(response)
            })
        },
        sentTransaction(){
            axios.post('/api/transactions?amount=' + this.amount + '&description=' + this.description + '&originAccount=' + this.originAccount + '&targetAccount=' + this.targetAccount)
            .then(response => {
                
                console.log(response),
                window.location.href = "../pages/accounts.html"
            })
            .catch(error => {
                
                this.error = error.response.data,
                window.location.href = "../pages/transfers.html"}

            )
        },
        logOut(){
            axios('/api/logout')
            .then(
                window.location.href = "../index.html"
            )
        }
    }
}).mount('#app')