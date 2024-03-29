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
            amount : "",
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
        getBalance(accountNumber) {
            const selectedAccount = this.accounts.find(account => account.number === accountNumber);
            return selectedAccount ? selectedAccount.balance : "Unable to retrieve account balance.";
        },
        sentTransaction() {
            const confirmationMessage = `Are you sure you want to transfer $${this.amount} to ${this.targetAccount}?`;
        
            Swal.fire({
                title: "Complete transaction?",
                text: confirmationMessage,
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Proceed"
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/transactions', {
                        "amount": this.amount,
                        "description": this.description,
                        "originAccount": this.originAccount,
                        "targetAccount": this.targetAccount
                    })
                        .then(response => {
                            Swal.fire({
                                title: "Success",
                                text: "Transaction Completed",
                                icon: "success"
                            });
                            this.clearData();
                            console.log(response);
                        })
                        .catch(error => {
                            this.error = error.response.data;
                        });
                }
            });
        },
        logOut(){
            axios('/api/logout')
            .then(
                window.location.href = "../index.html"
            )
        },
        clearData(){
            this.amount = '',
            this.description = '',
            this.originAccount = '',
            this.targetAccount = '',
            this.error = ''
        }
    }
}).mount('#app')