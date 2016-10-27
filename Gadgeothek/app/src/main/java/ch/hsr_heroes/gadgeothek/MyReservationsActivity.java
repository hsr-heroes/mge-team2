package ch.hsr_heroes.gadgeothek;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.hsr_heroes.gadgeothek.domain.Reservation;
import ch.hsr_heroes.gadgeothek.service.Callback;
import ch.hsr_heroes.gadgeothek.service.LibraryService;


public class MyReservationsActivity extends BaseListActivity {

    private ReservationsAdapter reservationsAdapter;

    @Override
    protected void onCreateMainContent(ViewGroup contentView) {
        super.onCreateMainContent(contentView);

        reservationsAdapter = new ReservationsAdapter();
        recyclerView.setAdapter(reservationsAdapter);
        LibraryService.getReservationsForCustomer(new Callback<List<Reservation>>() {
            @Override
            public void onCompletion(List<Reservation> input) {
                reservationsAdapter.setReservationList(input);
                if(input.isEmpty()){
                    setEmptyMessage("No Reservations. You can reserve gadgets in the All Gadgets menu :-))");
                }else{
                    clearEmptyMessage();
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MyReservationsActivity.this, "Error Loading Reservations "+message, Toast.LENGTH_LONG).show();
                setEmptyMessage(message);
            }
        });
    }

    private class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationViewHolder>{

        private final List<Reservation> reservations = new ArrayList<>();
        @Override
        public ReservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ReservationViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_reservation, parent, false));
        }

        @Override
        public void onBindViewHolder(ReservationViewHolder holder, int position) {
            Reservation r = reservations.get(position);
            holder.name.setText(r.getGadget().getName());
        }

        @Override
        public int getItemCount() {
            return reservations.size();
        }

        public void setReservationList(List<Reservation> input) {
            reservations.clear();
            reservations.addAll(input);
            notifyDataSetChanged();
        }

        class ReservationViewHolder extends RecyclerView.ViewHolder{

            private final TextView name;

            public ReservationViewHolder(View itemView) {
                super(itemView);
                this.name = (TextView) itemView.findViewById(R.id.reserved_gadget_name);
            }
        }

    }
}
