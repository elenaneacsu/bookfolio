package com.elenaneacsu.bookfolio.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elenaneacsu.bookfolio.R
import com.elenaneacsu.bookfolio.databinding.FragmentFavouritesBinding
import com.elenaneacsu.bookfolio.extensions.*
import com.elenaneacsu.bookfolio.models.Quote
import com.elenaneacsu.bookfolio.ui.MainActivity
import com.elenaneacsu.bookfolio.utils.setOnOneOffClickListener
import com.elenaneacsu.bookfolio.view.fragment.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Elena Neacsu on 13/06/21
 */
@AndroidEntryPoint
class FavouritesFragment: BaseMvvmFragment<FavouritesViewModel, FragmentFavouritesBinding>(
    R.layout.fragment_favourites, FavouritesViewModel::class.java
) {

    private var quotesAdapter: QuotesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        (activity as? MainActivity)?.apply {
            updateStatusBarColor(R.color.primary, false)
            viewBinding.pullToRefresh.setColorSchemeColors(getThemeColor(R.attr.colorAccent))
        }
        return view
    }


    override fun initViews() {
        super.initViews()

        context?.let {
            quotesAdapter = QuotesAdapter(it)
        }

        viewBinding.apply {
            toolbar.navigationIcon = null

            quotesRecyclerView.adapter = quotesAdapter

            temporaryFab.setOnOneOffClickListener {
                showDialogToAddQuote()
            }
        }

        mockData()
    }

    override fun hideProgress() {
        viewBinding.apply {
            pullToRefresh.isRefreshing = false
            viewOverlay.visibility = View.GONE
        }
    }

    override fun showProgress() {
        viewBinding.apply {
            pullToRefresh.isRefreshing = true
            viewOverlay.visibility = View.VISIBLE
        }
    }

    override fun errorAlert(message: String) {
    }

    override fun successAlert(message: String) {
    }

    private fun mockData() {
        val quote1 = Quote("Deschizi o ușă și apare alta, apoi încă una și încă una, până la ultima, care nici nu există măcar, și tot așa până te pomenești la prima, care nici nu există măcar, și mai dai o raită prin locuri vechi pentru că ceea ce credeai că te scosese și chiar te scosese devine capcană și te readuce tot acolo ca să înțelegi odată că ultimul tău adevăr e la fel de iluzoriu pe cât fusese primul și ca să nu uiți că te afli totdeauna pe muchie de cuțit.",
        "Zenobia", "Gellu Naum", "page 177", "June 1, 2021")

        val quote2 = Quote("On essaye de créer des liens, vous comprenez...\n" +
                "Mais oui, je comprenais. Dans cette vie qui vous apparaît quelquefois comme un grand terrain vague sans poteau indicateur, au milieu de toutes les lignes de fuite et les horizons perdus, on aimerait trouver des points de repère, dresser une sorte de cadastre pour n’avoir plus l’impression de naviguer au hasard. Alors, on tisse des liens, on essaye de rendre plus stable des rencontres hasardeuses.",
            "Dans le café de la jeunesse perdue", "Patrick Modiano", "page 55", "June 12, 2021")

        val quote3 = Quote("You know what's the trouble with you? You're an expatriate. One of the worst type. You've lost touch with the soil. You get precious. Fake European standards have ruined you. You drink yourself to death. You become obsessed by sex. You spend all your time talking, not working. You are an expatriate, see. You hang around cafés.\nIt sounds like a swell life.",
            "Fiesta: The Sun Also Rises", "Ernest Hemingway", "page 101", "June 25, 2021")

        val quote4 = Quote("there is a place in the heart that\n" +
                "will never be filled\n" +
                "a space\n" +
                "and even during the\n" +
                "best moments\n" +
                "and\n" +
                "the greatest\n" +
                "times\n" +
                "we will know it\n" +
                "we will know it\n" +
                "more than\n" +
                "ever\n" +
                "there is a place in the heart that\n" +
                "will never be filled\n" +
                "and\n" +
                "we will wait\n" +
                "and\n" +
                "wait\n" +
                "in that\n" +
                "space.", "You Get So Alone At Times That It Just Makes Sense",
        "Charles Bukowski", "page 24", "June 27, 2021")

        val quote5 = Quote("Acum timpul era solubil: săptămânile și lunile se uneau intr-o curgere continuă la o viteză tot mai mare. Lucra la fel de mult, dar pentru el însuși timpul se modificase, iar limitele lui arbitrare erau neclare. Anii nu mai erau blocuri individuale și Alpi Apuseni, pe care omul îi spărgea in coloane separate. Oare săptămânile și lunile erau mai scurte ca durată, sau încetase el să mai numere, folosind o altă măsură? În trecut, timpul avusese o calitate sfărâmicioasă, fusese solid. Acum era fluid. Peisajul lui devenise atât de diferit pentru el, pe cât era diferită campagna romană de Toscana. Își închipuise că timpul era absolut, la fel peste tot, întotdeauna, pentru toți oamenii. Acum văzu ca era la fel de schimbător precum firea omului sau vremea pe cer. Pe măsură ce 1531 devenea 1532 și 1532 începea să se micșoreze și să devină 1533, se întreba: Unde se duce timpul?\n" +
                "Răspunsul era destul de simplu: fusese schimbat dintr-un lucru amorf in ceva concret, devenind parte din vitalitatea \"Madonei cu pruncul\", a \"Aurorei\" și a \"Amurgului\", a tinerilor Medici. Ce nu înțelesese el era că timpul se scurta doar aparent, asemenea spațiului. Când era pe vârful unui deal, privind peste valea Toscanei din fața lui, jumătate din aceasta era vizibilă in detalii clare. Jumătatea mai îndepărtată, chiar dacă la fel de largă, părea strânsă, înghesuită, asemenea unei fâșii înguste mai degrabă decât un câmp întins. La fel se întâmpla și cu timpul din zonele mai îndepărtate ale vieții unui om. Indiferent cât de atent cerceta orele și zilele care treceau, acestea păreau mai scurte atunci când erau comparate cu prima parte larg desfășurată a vieții lui.",
        "Agonie si extaz", "Irving Stone", "page 343", "March 21, 2021")

        val quote6 = Quote("In the state I was in, if someone had come and told me I could go home quietly, that they would leave me my life whole, it would have left me cold: several hours or several years of waiting is all the same when you have lost the illusion of being eternal.",
        "The Wall", "Jean-Paul Sartre", "page 87", "January 20, 2021")

        val quote7 = Quote("some men never\n" +
                "die\n" +
                "and some men never\n" +
                "live\n" +
                "\n" +
                "but we're all alive\n" +
                "tonight.", page = "page 23", date = "June 18, 2021")

        val quote8 = Quote("each man's hell is in a different\n" +
                "place: mine is just up and\n" +
                "behind\n" +
                "my ruined\n" +
                "face.", page = "page 23", date = "June 15, 2021")
//        val quotes = listOf(quote1, quote2, quote3, quote4, quote5, quote6)
        val quotes = listOf(quote4, quote7, quote8)


        quotesAdapter?.add(quotes)
        quotesAdapter?.notifyDataSetChanged()
    }

    private fun showDialogToAddQuote() {
        context?.let { ctx ->
            ctx.alert(cancelable = true, style = R.style.AlertDialogStyle) {
                setView(R.layout.add_quote_dialog_layout)

                positiveButton("Save") {

                }

                negativeButton("Cancel") {

                }
            }
        }
    }
}