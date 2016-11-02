package ch.hsr_heroes.gadgeothek;

import android.content.Intent;
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

import ch.hsr_heroes.gadgeothek.domain.Gadget;
import ch.hsr_heroes.gadgeothek.service.Callback;
import ch.hsr_heroes.gadgeothek.service.LibraryService;

public class GadgetListActivity extends BaseListActivity {

    private GadgetAdapter gadgetAdapter;

    protected void onCreateMainContent(ViewGroup contentView) {
        super.onCreateMainContent(contentView);
        setActiveMenuItem(R.id.nav_all_gadgets);
        gadgetAdapter = new GadgetAdapter();
        recyclerView.setAdapter(gadgetAdapter);
        loadGadgets();
    }

    private void loadGadgets() {
        LibraryService.getGadgets(new Callback<List<Gadget>>() {
            @Override
            public void onCompletion(List<Gadget> input) {
                if (!input.isEmpty()) {
                    gadgetAdapter.setGadgetList(input);
                    clearEmptyMessage();
                } else {
                    setEmptyMessage(getString(R.string.no_gadgets_yet));
                }
            }

            @Override
            public void onError(String message) {
                String errorMessage = getString(R.string.error_loading_gadgets,  message);
                showToastMessage(errorMessage);
                setEmptyMessage(errorMessage);

                ((GadgetAdapter) recyclerView.getAdapter()).clearGadgetList();
            }
        });
    }

    @Override
    void refreshList() {
        loadGadgets();
    }

    private class GadgetAdapter extends RecyclerView.Adapter<GadgetAdapter.GadgetViewHolder> {

        private final List<Gadget> gadgets = new ArrayList<>();

        @Override
        public GadgetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new GadgetViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_gadget, parent, false));
        }

        @Override
        public void onBindViewHolder(GadgetViewHolder holder, int position) {
            final Gadget g = gadgets.get(position);
            holder.name.setText(g.getName());
            holder.manufacturer.setText(g.getManufacturer());

            holder.buttonReserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LibraryService.reserveGadget(g, new Callback<Boolean>() {
                        @Override
                        public void onCompletion(Boolean reserved) {
                            if (reserved) {
                                startActivity(new Intent(GadgetListActivity.this, MyReservationsActivity.class));
                                showToastMessage(R.string.reservation_successful);
                            } else {
                                showToastMessage(R.string.reservation_failed);
                            }
                        }

                        @Override
                        public void onError(String message) {
                            showToastMessage(getString(R.string.unable_to_reserve_gadget));
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return gadgets.size();
        }

        public void setGadgetList(List<Gadget> input) {
            gadgets.clear();
            gadgets.addAll(input);
            notifyDataSetChanged();
        }

        public void clearGadgetList() {
            gadgets.clear();
            notifyDataSetChanged();
        }

        class GadgetViewHolder extends RecyclerView.ViewHolder {
            private final TextView name;
            private final TextView manufacturer;
            private final ImageView buttonReserve;

            public GadgetViewHolder(View itemView) {
                super(itemView);
                this.name = (TextView) itemView.findViewById(R.id.my_gadget_name);
                this.manufacturer = (TextView) itemView.findViewById(R.id.gadget_manufacturer);
                this.buttonReserve = (ImageView) itemView.findViewById(R.id.button_reserve);
            }
        }

    }
}
