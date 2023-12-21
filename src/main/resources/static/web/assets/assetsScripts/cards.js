const {createApp} = Vue

let app = createApp({
    data(){
        return {
            data: [],
            client: [],
            firstName : "",
            lastName : "",
            email : "",
            cards : []
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
                this.cards = response.data.cards,
                console.log(this.cards)
            })
            .catch(error => console.log(error))
        }
        
    }
}).mount('#app')
