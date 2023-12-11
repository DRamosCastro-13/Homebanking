const{createApp} = Vue

let app = createApp({
    data(){
        return {
            data : [],
            clients : [],
            firstName : "",
            lastName : "",
            email : "", 
        }
    },
    created(){
        this.loadData()
    },
    
    methods: {
        loadData(){
            axios('/clients')
            .then(response =>{
                this.data = response
                this.clients = response.data._embedded.clients
                console.log(this.clients)
            })
            .catch(error => console.log(error))
        },
        createClient(){
            axios.post('/api/cilents/1',
            {
            "firstName" : this.firstName,
            "lastName" : this.lastName,
            "email": this.email})
            .then(response => {
                console.log(response)
                this.loadData()
            })
            .catch(error => console.log(error))
        }
    
    },

}).mount('#app')