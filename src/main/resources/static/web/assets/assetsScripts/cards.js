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
            cardId : null
           
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
        activeCards() {
            return this.cards.filter(card => !card.deleted);
        },
        deleteCard() {
            axios.delete(`/api/cards/${this.cardId}`)
                .then(response => {
                    Swal.fire({
                        title: "Complete transaction?",
                        text: "You won't be able to revert this!",
                        icon: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#3085d6",
                        cancelButtonColor: "#d33",
                        confirmButtonText: "Proceed"
                    }).then((result) => {
                        if (result.isConfirmed) {
                            
                            Swal.fire({
                                title: "Success",
                                text: "Transaction Completed",
                                icon: "success"
                            });
        
                            setTimeout(() => {
                                window.location.reload();
                            }, 3000);
                        }
                    })
                    .catch(error => console.log(error));
                });
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
