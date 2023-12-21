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
            axios('/api/clients/1')
            .then(response =>{ 
                this.data = response,
                this.client = response.data,
                this.accounts = response.data.accounts,
                this.loans = response.data.loans,
                console.log(this.loans)
            })
            .catch(error => console.log(error))
        }
    }
}).mount('#app')


// createClient(){
        //     axios.post('/api/cilents/1',
        //     {
        //     "firstName" : this.firstName,
        //     "lastName" : this.lastName,
        //     "email": this.email,
        //     "accounts": this.accounts})
        //     .then(response => {
        //         console.log(response)
        //         this.loadData()
        //     })
        //     .catch(error => console.log(error))
        // }