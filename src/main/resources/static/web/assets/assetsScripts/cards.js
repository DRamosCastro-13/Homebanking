const {createApp} = Vue

let app = createApp({
    data(){
        return {
            data: [],
            client: [],
            firstName : "",
            lastName : "",
            email : "",
            cards : [],
           
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
                this.cards = response.data.cards,
                console.log(this.cards)
            })
            .catch(error => console.log(error))
        },
        logOut(){
            axios('/api/logout')
            .then(
                window.location.href = "../index.html"
            )
        },
        createCard(){
            window.location.href="../pages/createCards.html"
        }
    }
}).mount('#app')
