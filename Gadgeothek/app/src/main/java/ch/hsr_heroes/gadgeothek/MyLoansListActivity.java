package ch.hsr_heroes.gadgeothek;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        setActiveMenuItem(R.id.nav_my_gadgets);
        loanAdapter = new LoanAdapter();
        recyclerView.setAdapter(loanAdapter);
        loadLoans();
    }

    private void loadLoans() {
        LibraryService.getLoansForCustomer(new Callback<List<Loan>>() {
            @Override
            public void onCompletion(List<Loan> input) {
                if (!input.isEmpty()) {
                    loanAdapter.setLoanList(input);
                    clearEmptyMessage();
                } else {
                    setEmptyMessage(getString(R.string.no_loans_yet));
                }
            }

            @Override
            public void onError(String message) {
                String errorMessage = getString(R.string.error_loading_loans, message);
                showToastMessage(errorMessage);
                setEmptyMessage(errorMessage);

                ((LoanAdapter) recyclerView.getAdapter()).clearLoanList();
            }
        });
    }

    @Override
    void refreshList() {
        loadLoans();
    }

    private class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.LoanViewHolder> {

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

            if (overDueDate != null) {
                holder.overdueDate.setText(getString(R.string.return_date, dateFormat.format(overDueDate)));
            } else {
                holder.overdueDate.setText(R.string.failed_loading_return_date);
            }

            if (l.isOverdue()) {
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

        public void clearLoanList() {
            loans.clear();
            notifyDataSetChanged();
        }

        class LoanViewHolder extends RecyclerView.ViewHolder {

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
