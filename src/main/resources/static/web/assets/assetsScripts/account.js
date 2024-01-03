const {createApp} = Vue

let app = createApp({
    data(){
        return {
            account: [],
            firstName : "",
            lastName : "",
            email : "",
            accounts : [],
        }
    },
    created(){
        this.loadData()
    },

    methods: {
        loadData(){
            const url = new URLSearchParams(window.location.search)
            axios('/api/accounts/id=' + url.get('id'))
            .then(response =>{ 
                this.account = response.data,
                
                console.log(this.account)
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