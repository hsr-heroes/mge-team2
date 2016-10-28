package ch.hsr_heroes.gadgeothek;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.hsr_heroes.gadgeothek.domain.Loan;
import ch.hsr_heroes.gadgeothek.service.Callback;
import ch.hsr_heroes.gadgeothek.service.LibraryService;

public class MyLoansListActivity extends BaseListActivity {

    private LoanAdapter loanAdapter;

    protected void onCreateMainContent(ViewGroup contentView) {
        super.onCreateMainContent(contentView);

        loanAdapter = new LoanAdapter();
        recyclerView.setAdapter(loanAdapter);
        loadLoans();
    }

    private void loadLoans() {
        LibraryService.getLoansForCustomer(new Callback<List<Loan>>() {
        @Override
        public void onCompletion(List<Loan> input) {
            loanAdapter.setLoanList(input);
            if(input.isEmpty()){
                setEmptyMessage("No Loans Yet. There is cool stuff to be tested. visit the library");
            }else{
                clearEmptyMessage();
            }
        }

        @Override
        public void onError(String message) {
            Toast.makeText(MyLoansListActivity.this, "Error Loading Loans "+message, Toast.LENGTH_LONG).show();
            setEmptyMessage( "Error Loading Loans "+message);
        }
    });
    }

    @Override
    void refreshList() {
        loadLoans();
    }

    private class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.LoanViewHolder>{

        private final List<Loan> loans = new ArrayList<>();
        @Override
        public LoanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LoanViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_loan, parent, false));
        }

        @Override
        public void onBindViewHolder(LoanViewHolder holder, int position) {
            Loan l = loans.get(position);

            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
            Date overDueDate = l.overDueDate();

            holder.name.setText(l.getGadget().getName());

            if(overDueDate != null) {
                holder.overdueDate.setText("Return Date: " + dateFormat.format(overDueDate));
            } else {
                holder.overdueDate.setText("Failed to load return date");
            }

            if(l.isOverdue()) {
                holder.overdue.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return loans.size();
        }

        public void setLoanList(List<Loan> input) {
            loans.clear();
            loans.addAll(input);
            notifyDataSetChanged();
        }

        class LoanViewHolder extends RecyclerView.ViewHolder{

            private final TextView name;
            private final TextView overdueDate;
            private final TextView overdue;

            public LoanViewHolder(View itemView) {
                super(itemView);

                this.name = (TextView) itemView.findViewById(R.id.loan_gadget_name);
                this.overdueDate = (TextView) itemView.findViewById(R.id.overdue_date);
                this.overdue = (TextView) itemView.findViewById(R.id.overdue);
            }
        }

    }
}
