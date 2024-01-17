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
            accountId : null,
            showAccountSelectionModal: false,
            selectedAccountId: null,
            isModalVisible: false
            
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
            axios.post('/api/accounts/clients/current')
            .then(response => {
                Swal.fire({
                    title: "Success",
                    text: "Transaction Completed",
                    icon: "success"
                });

                setTimeout(() => {
                    window.location.reload();
                }, 3000);
            })
        },
        openAccountSelectionModal() {
            this.showAccountSelectionModal = true;
          },
        onToggle() {
            this.isModalVisible = true;
        },
        deleteAccount(){
            axios.delete(`/api/accounts/${this.accountId}`)
            .then(response => {
                
            })
        },
        confirmDeleteCard() {
            Swal.fire({
              title: "Confirm Deletion",
              text: "Are you sure you want to delete this card? This action cannot be undone.",
              icon: "warning",
              showCancelButton: true,
              confirmButtonColor: "#d33",
              cancelButtonColor: "#3085d6",
              confirmButtonText: "Delete Card"
            }).then((result) => {
              if (result.isConfirmed) {
                this.deleteCard();
              }
            });
          },

    }
}).mount('#app')

