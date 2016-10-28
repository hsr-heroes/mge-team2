package ch.hsr_heroes.gadgeothek;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        loadReservations();
    }

    private void loadReservations() {
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

    @Override
    void refreshList() {
        loadReservations();
    }

    private class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationViewHolder>{

        private final List<Reservation> reservations = new ArrayList<>();
        @Override
        public ReservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ReservationViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_reservation, parent, false));
        }

        @Override
        public void onBindViewHolder(ReservationViewHolder holder, final int position) {
            final Reservation r = reservations.get(position);
            holder.name.setText(r.getGadget().getName());
            holder.waitingPosition.setText("Waiting Position: " + r.getWatingPosition());
            holder.manufacturer.setText(r.getGadget().getManufacturer());

            holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LibraryService.deleteReservation(r, new Callback<Boolean>() {
                        @Override
                        public void onCompletion(Boolean deleted) {
                            if(deleted) {
                                Toast.makeText(MyReservationsActivity.this, R.string.deletion_successful, Toast.LENGTH_LONG).show();
                                notifyItemRemoved(position);
                            } else {
                                Toast.makeText(MyReservationsActivity.this, R.string.deletion_failed, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(MyReservationsActivity.this, getString(R.string.unable_to_delete_reservation) + "\n" + message, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
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
            private final TextView waitingPosition;
            private final TextView manufacturer;
            private final ImageView buttonDelete;

            public ReservationViewHolder(View itemView) {
                super(itemView);
                this.name = (TextView) itemView.findViewById(R.id.reserved_gadget_name);
                this.waitingPosition = (TextView) itemView.findViewById(R.id.waiting_position);
                this.manufacturer = (TextView) itemView.findViewById(R.id.gadget_manufacturer);
                this.buttonDelete = (ImageView) itemView.findViewById(R.id.button_delete);
            }
        }

    }
}
