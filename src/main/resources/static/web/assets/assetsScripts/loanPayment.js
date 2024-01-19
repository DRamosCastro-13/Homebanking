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
            availableLoans : [],
            loans : [],
            selectedLoan : '',
            name : '',
            amount : '',
            payments : '',
            originAccount : '',
            error : "",
            selectedAccount: '',
            accountNumber : '',
        }

    },
    created(){
        this.loadLoan()
        this.loadData()
        
    },

    methods: {
        loadData(){
            axios('/api/clients/current')
            .then(response =>{ 
                this.data = response,
                this.client = response.data,
                this.accounts = response.data.accounts,
                this.loans = response.data.loans
                console.log(this.loans)
            })
            .catch(error => console.log(error))
        },
        loadLoan(){
            axios('/api/loans/availableLoans')
            .then(response =>{
                this.data = response,
                this.availableLoans = response.data,
                console.log(this.availableLoans)
            })
            .catch(error => console.log(error))
        },
        payLoan() {
            this.error = "";
        
            if (this.selectedLoan == null || !this.amount || !this.originAccount) {
                this.error = "Please fill in all required fields.";
                return;
            }

            const selectedLoan = this.loans.find(loan => loan.id === this.selectedLoan);
            const loanName = selectedLoan ? selectedLoan.name : "Unknown Loan";

            const confirmationMessage = `You are about to pay ${this.amount.toLocaleString("en-US", { style: "currency", currency: "USD" })} from account ${this.originAccount} towards your ${loanName}. Proceed?`;
        
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
                    axios.post('/api/loans/payment', {
                        "id": this.selectedLoan,
                        "amount": this.amount,
                        "account": this.originAccount
                    })
                        .then(response => {
                            Swal.fire({
                                title: "Success",
                                text: "Successful payment",
                                icon: "success"
                            });
                            this.clearData();
                            console.log(response);

                            setTimeout(() => {
                                window.location.href="../pages/accounts.html"
                            }, 2000);
                        })
                        .catch(error => {
                            this.error = error.response.data;
                        });
                }
            });
        },
        getBalance(accountNumber) {
            const selectedAccount = this.accounts.find(account => account.number === accountNumber);
            return selectedAccount ? selectedAccount.balance : "Unable to retrieve account balance.";
        },
        getDebt(loanId) {
            const selectedLoan = this.loans.find(loan => loan.id === loanId);
            return selectedLoan ? selectedLoan.amount : "Unable to retrieve loan debt.";
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