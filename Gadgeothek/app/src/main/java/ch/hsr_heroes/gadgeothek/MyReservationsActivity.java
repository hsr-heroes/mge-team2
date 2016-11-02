package ch.hsr_heroes.gadgeothek;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        setActiveMenuItem(R.id.nav_my_reservations);
        reservationsAdapter = new ReservationsAdapter();
        recyclerView.setAdapter(reservationsAdapter);
        loadReservations();
    }

    private void loadReservations() {
        LibraryService.getReservationsForCustomer(new Callback<List<Reservation>>() {
            @Override
            public void onCompletion(List<Reservation> input) {
                if (!input.isEmpty()) {
                    reservationsAdapter.setReservationList(input);
                    clearEmptyMessage();
                } else {
                    setEmptyMessage(getString(R.string.no_reservations_found));
                }
            }

            @Override
            public void onError(String message) {
                String errorMessage = getString(R.string.error_loading_reservations, message);
                showToastMessage(errorMessage);
                setEmptyMessage(errorMessage);

                ((ReservationsAdapter) recyclerView.getAdapter()).clearReservationList();
            }
        });
    }

    @Override
    void refreshList() {
        loadReservations();
    }

    private class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationViewHolder> {

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
            holder.waitingPosition.setText(getString(R.string.waiting_position, r.getWatingPosition()));
            holder.manufacturer.setText(r.getGadget().getManufacturer());

            holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(MyReservationsActivity.this).setMessage(getString(R.string.really_delete_reservation, r.getGadget().getName()))
                            .setPositiveButton(R.string.delete_reservation, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteReservation();
                                }
                            }).setNegativeButton(R.string.abort_action, null).setCancelable(true).show();
                }

                private void deleteReservation() {
                    LibraryService.deleteReservation(r, new Callback<Boolean>() {
                        @Override
                        public void onCompletion(Boolean deleted) {
                            if (deleted) {
                                showToastMessage(R.string.deletion_successful);
                                reservations.remove(position);
                                notifyItemRemoved(position);

                                if(getItemCount() == 0) {
                                    setEmptyMessage(getString(R.string.no_reservations_found));
                                }
                            } else {
                                showToastMessage(R.string.deletion_failed);
                            }
                        }

                        @Override
                        public void onError(String message) {
                            showToastMessage(getString(R.string.unable_to_delete_reservation, message));
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

        public void clearReservationList() {
            reservations.clear();
            notifyDataSetChanged();
        }

        class ReservationViewHolder extends RecyclerView.ViewHolder {

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
