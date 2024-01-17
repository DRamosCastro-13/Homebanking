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
            type : '',
            color : '',
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
                this.cards = response.data.cards,
                console.log(this.cards)
            })
            .catch(error => console.log(error))
        },
        createCard(){
            axios.post('/api/cards/clients/current',{
                "type" : this.type,
                "color" : this.color
            })
            .then(response => {
                Swal.fire({
                    title: "Success",
                    text: "Transaction Completed",
                    icon: "success"
                });

                setTimeout(() => {
                    window.location.href="../pages/cards.html"
                }, 2000);
                
            })
            .catch(error => {
                console.log(error)
                this.error = error.response.data}
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